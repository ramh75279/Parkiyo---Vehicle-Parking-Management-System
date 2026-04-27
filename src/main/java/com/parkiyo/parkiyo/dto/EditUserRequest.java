package com.parkiyo.parkiyo.dto;   // make sure package is correct

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;  // optional but recommended

@Data
public class EditUserRequest {

    @Email(message = "Invalid email format")
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String role;        // or whatever fields you allow for update

    // You can add more fields as needed (e.g. status, etc.)
}