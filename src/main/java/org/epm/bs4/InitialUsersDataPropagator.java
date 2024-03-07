package org.epm.bs4;

import lombok.AllArgsConstructor;
import org.epm.bs4.model.Authority;
import org.epm.bs4.model.User;
import org.epm.bs4.repository.AuthorityRepository;
import org.epm.bs4.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class InitialUsersDataPropagator implements ApplicationRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public static final String INFO_PERMISSION = "VIEW_INFO";
    public static final String ADMIN_PERMISSION = "VIEW_ADMIN";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() < 1) {
            userRepository.save(new User("anon@epm.org", passwordEncoder.encode("passw0rd"), true));

            var user = userRepository.save(new User("user@epm.org", passwordEncoder.encode("passw0rd"), true));
            authorityRepository.save(new Authority(INFO_PERMISSION, user));

            var admin = userRepository.save(new User("admin@epm.org", passwordEncoder.encode("passw0rd"), true));
            authorityRepository.save(new Authority(INFO_PERMISSION, admin));
            authorityRepository.save(new Authority(ADMIN_PERMISSION, admin));
        }
    }
}