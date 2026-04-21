package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.service.AuthService;
import com.parkiyo.parkiyo.service.AuditLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // enables @PreAuthorize on your controllers
public class SecurityConfig {

        private final AuthService authService;
        private final AuditLogService auditLogService;

        public SecurityConfig(AuthService authService, AuditLogService auditLogService) {
                this.authService = authService;
                this.auditLogService = auditLogService;
        }

    // ─── Public pages (no login required) ────────────────────────────────────
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
            "/access-denied",
            "/css/**",
            "/js/**",
            "/images/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(authService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")               // your custom login page
                        .loginProcessingUrl("/login")      // Spring handles POST /login
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
                        .logoutUrl("/logout")
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

    // BCrypt password encoder — use this everywhere you save/check passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}