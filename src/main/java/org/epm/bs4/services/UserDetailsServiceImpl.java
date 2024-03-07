package org.epm.bs4.services;

import lombok.AllArgsConstructor;
import org.epm.bs4.config.UserDetailsConfig;
import org.epm.bs4.exception.UserBlockedException;
import org.epm.bs4.model.User;
import org.epm.bs4.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                        .findById(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        updateBlockedStatus(user);

        return new UserDetailsConfig(user);
    }

    private void updateBlockedStatus(User user) {
        var isBlocked = loginAttemptService.isBlocked(user);
        user.setEnabled(!isBlocked);
        userRepository.save(user);

        if (isBlocked) {
            throw new UserBlockedException("User " + user.getEmail() + " is blocked.");
        }
    }
}