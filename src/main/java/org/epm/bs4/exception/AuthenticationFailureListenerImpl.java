package org.epm.bs4.exception;

import lombok.AllArgsConstructor;
import org.epm.bs4.services.LoginService;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthenticationFailureListenerImpl implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    private final LoginService loginAttemptService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        var username = String.valueOf(((UsernamePasswordAuthenticationToken) event.getSource()).getPrincipal());
        loginAttemptService.loginFailed(username);
    }
}