package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LoginRequest {

    @Getter @Setter
    public class LoginRequest {
        @Email
        @NotBlank
        private String email;

        @NotBlank
        private String password;

        private boolean rememberMe;
    }
}
