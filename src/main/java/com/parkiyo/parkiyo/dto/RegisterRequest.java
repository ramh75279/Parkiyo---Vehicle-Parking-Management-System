package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    private String phone;
    @NotBlank private String password;
    @NotBlank private String confirmPassword;

    // Alias so the HTML form field "confirmPw" binds to confirmPassword
    public void setConfirmPw(String confirmPw) {
        this.confirmPassword = confirmPw;
    }
}