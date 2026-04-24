package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Role role;
}
