package org.epm.bs4.config;

import lombok.AllArgsConstructor;
import org.epm.bs4.exception.AuthenticationFailureHandlerImpl;
import org.epm.bs4.repository.UserRepository;
import org.epm.bs4.services.LoginService;
import org.epm.bs4.services.UserDetailsServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    private final UserRepository userRepository;
    private final LoginService loginService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository, LoginService loginService, ApplicationEventPublisher applicationEventPublisher) throws Exception {

        http.authenticationProvider(getAuthenticationProvider(userRepository, loginService)).authorizeHttpRequests(authz -> authz.requestMatchers("/info").hasAuthority("VIEW_INFO").requestMatchers("/admin").hasAuthority("VIEW_ADMIN").requestMatchers("/about").permitAll().anyRequest().authenticated()).formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/index").failureUrl("/login?error").and().logout().clearAuthentication(true).invalidateHttpSession(true).logoutSuccessUrl("/login?logout").permitAll();
        return http.build();
    }

    @Bean
    public UserDetailsService getUserDetailsService(UserRepository userRepository, LoginService loginService) {
        return new UserDetailsServiceImpl(userRepository, loginService);
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider(UserRepository userRepository, LoginService loginService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(getUserDetailsService(userRepository, loginService));
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationFailureHandler getAuthenticationFailureHandler(ApplicationEventPublisher applicationEventPublisher) {
        AuthenticationFailureHandlerImpl handler = new AuthenticationFailureHandlerImpl();
        handler.setEventPublisher(applicationEventPublisher);
        return handler;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}