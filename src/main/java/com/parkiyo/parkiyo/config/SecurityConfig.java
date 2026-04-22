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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthService authService;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    private static final String[] PUBLIC_URLS = {
            "/",
            "/home",
            "/features",
            "/solutions",
            "/analytics",
            "/faq",
            "/privacy",
            "/login",
            "/register",
            "/forgot-password",
            "/reset-password",
            "/access-denied",
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**"
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")

                        // 🔥 THIS IS THE FIX
                        .usernameParameter("email")
                        .passwordParameter("password")

                        .successHandler((request, response, authentication) -> {
                            auditLogService.logAction(
                                    "LOGIN",
                                    authentication.getName(),
                                    "Authentication",
                                    null,
                                    "User logged in successfully",
                                    null,
                                    request.getRemoteAddr(),
                                    request.getHeader("User-Agent")
                            );
                            response.sendRedirect(request.getContextPath() + "/dashboard");
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/perform-logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}
