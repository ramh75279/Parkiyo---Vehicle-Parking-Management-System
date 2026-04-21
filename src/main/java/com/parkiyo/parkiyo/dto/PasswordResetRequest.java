package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordResetRequest {
    @Email
    @NotBlank
    private String email;

    private String token;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}
