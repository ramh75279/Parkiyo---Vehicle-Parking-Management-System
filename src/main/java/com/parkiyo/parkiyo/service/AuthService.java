package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.PasswordResetRequest;
import com.parkiyo.parkiyo.dto.RegisterRequest;
import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Wallet;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int RESET_TOKEN_BYTES = 32;
    private static final int RESET_TOKEN_EXPIRY_MINUTES = 30;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetDeliveryService passwordResetDeliveryService;
    private final JavaMailSender mailSender;

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${parkiyo.security.require-email-verification:true}")
    private boolean requireEmailVerification;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        if (user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.SUSPENDED) {
            throw new UsernameNotFoundException("Account is " + user.getStatus().name().toLowerCase());
        }

        if (requireEmailVerification && !user.isEmailVerified()) {
            throw new UsernameNotFoundException("Email not verified. Please check your inbox.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("An account with this email already exists.");
        }

        String verificationToken = UUID.randomUUID().toString().replace("-", "");

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.USER)
                .status(UserStatus.ACTIVE)
                .emailNotificationsEnabled(true)
                .smsNotificationsEnabled(true)
                .emailVerified(false)
                .emailVerificationToken(verificationToken)
                .build();

        userRepository.save(user);

        Wallet wallet = Wallet.builder().user(user).balance(java.math.BigDecimal.ZERO).build();
        walletRepository.save(wallet);

        sendVerificationEmail(user.getEmail(), verificationToken);
    }

    private void sendVerificationEmail(String email, String token) {
        String baseUrl = appBaseUrl.endsWith("/") ?
                appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        String link = baseUrl + "/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Verify your Parkiyo account");
        message.setText("Welcome to Parkiyo!\n\n"
                + "Please verify your email address by clicking the link below:\n\n"
                + link + "\n\n"
                + "This link will work as soon as you click it.\n\n"
                + "If you did not create an account, please ignore this email.\n\n"
                + "— The Parkiyo Team");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.warn("Failed to send verification email to {}: {}", email, e.getMessage());
            // This method is called from a @Transactional register() method. Throwing here causes a rollback,
            // which prevents creating an account the user can never log into.
            throw new RuntimeException("We couldn't send the verification email. Please try again in a moment.");
        }
    }

    @Transactional
    public String verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or already used verification link."));

        if (user.isEmailVerified()) {
            return "already_verified";
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);

        return "success";
    }

    @Transactional
    public void resendVerificationEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.isEmailVerified()) {
                return;
            }
            String token = user.getEmailVerificationToken();
            if (token == null || token.isBlank()) {
                token = UUID.randomUUID().toString().replace("-", "");
                user.setEmailVerificationToken(token);
                userRepository.save(user);
            }
            sendVerificationEmail(user.getEmail(), token);
        });
    }

    @Transactional
    public void sendPasswordResetLink(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String token = generateResetToken();
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiry(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRY_MINUTES));
            userRepository.save(user);
            passwordResetDeliveryService.sendResetLink(user.getEmail(), buildResetLink(token));
        });
    }

    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset link."));

        if (user.getPasswordResetToken() == null
                || !user.getPasswordResetToken().equals(request.getToken())
                || user.getPasswordResetTokenExpiry() == null
                || user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired reset link.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiry(null);
        userRepository.save(user);
    }

    private String generateResetToken() {
        byte[] bytes = new byte[RESET_TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String buildResetLink(String token) {
        String baseUrl = appBaseUrl.endsWith("/") ?
                appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
    }

    @Transactional
    public void markSuccessfulLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);
        });
    }
}
