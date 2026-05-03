package com.parkiyo.parkiyo.dto;

import lombok.Data;

@Data
public class EditUserRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String role;
    private String email;
    private String status;
}