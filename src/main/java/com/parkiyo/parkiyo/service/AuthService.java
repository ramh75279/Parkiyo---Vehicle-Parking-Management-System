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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${parkiyo.app-base-url:http://localhost:8080}")
    private String appBaseUrl;

    // Spring Security calls this on every login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        if (user.getStatus() == UserStatus.INACTIVE || user.getStatus() == UserStatus.SUSPENDED) {
            throw new UsernameNotFoundException("Account is " + user.getStatus().name().toLowerCase());
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
                .build();
        userRepository.save(user);

        // Auto-create wallet for every new user
        Wallet wallet = Wallet.builder().user(user).build();
        walletRepository.save(wallet);
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
        String baseUrl = appBaseUrl.endsWith("/") ? appBaseUrl.substring(0, appBaseUrl.length() - 1) : appBaseUrl;
        return baseUrl + "/reset-password?token=" + token;
    }
}
