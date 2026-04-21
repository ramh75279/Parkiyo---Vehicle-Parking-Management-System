package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EditUserRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private Role role;
    private UserStatus status;
}
