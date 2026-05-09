package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.service.AuthService;
import com.parkiyo.parkiyo.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        // Allow us to surface specific auth failures (e.g., email not verified) on the login screen.
        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())

                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/home", "/features", "/solutions", "/analytics",
                                "/faq", "/privacy",
                                "/sign-in", "/perform-login", "/login",
                                "/register", "/forgot-password", "/reset-password",
                                "/verify-email",
                                "/access-denied",
                                "/uploads/**", "/css/**", "/js/**", "/images/**", "/webjars/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/assistant/**").hasAnyRole("ADMIN", "ASSISTANT")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/sign-in")
                        .loginProcessingUrl("/perform-login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            String username = authentication.getName();
                            authService.markSuccessfulLogin(username);

                            auditLogService.logAction(
                                    "LOGIN",
                                    username,
                                    "Authentication",
                                    null,
                                    "User logged in successfully",
                                    null,
                                    request.getRemoteAddr(),
                                    request.getHeader("User-Agent")
                            );
                            response.sendRedirect(request.getContextPath() + "/dashboard");
                        })
                        .failureHandler((request, response, exception) -> {
                            String msg = "Invalid email or password.";
                            if (exception != null && exception.getMessage() != null) {
                                String raw = exception.getMessage();
                                String lower = raw.toLowerCase();
                                if (lower.contains("email not verified")) {
                                    msg = "Email not verified. Please check your inbox (and spam) for the verification link.";
                                } else if (lower.contains("account is")) {
                                    msg = raw;
                                }
                            }
                            String encoded = URLEncoder.encode(msg, StandardCharsets.UTF_8);
                            response.sendRedirect(request.getContextPath() + "/sign-in?error=true&message=" + encoded);
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/perform-logout")
                        .logoutSuccessUrl("/sign-in?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/sign-in?expired=true")
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }
}
