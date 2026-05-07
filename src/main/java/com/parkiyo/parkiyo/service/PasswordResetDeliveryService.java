package com.parkiyo.parkiyo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetDeliveryService {

    private final JavaMailSender mailSender;

    @Value("${parkiyo.mail.from:no-reply@parkiyo.com}")
    private String fromAddress;

    public void sendResetLink(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(email);
        message.setSubject("Reset your Parkiyo password");
        message.setText("""
                We received a request to reset your Parkiyo password.

                Use this secure link within 30 minutes:
                %s

                If you did not request this, you can ignore this email.
                """.formatted(resetLink));

        try {
            mailSender.send(message);
        } catch (MailException ex) {
            log.warn("Password reset email could not be sent to {}. Configure SMTP before production use.", email, ex);
        }
    }
}
