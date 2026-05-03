package com.parkiyo.parkiyo.dto;

import lombok.Data;

@Data
public class EditUserRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String role;

    // Add this line
    private String email;   // Important for update
}