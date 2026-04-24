package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.Role;
import com.parkiyo.parkiyo.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EditUserRequest {
    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    private String phone;

    @NotNull(message = "Role is required.")
    private Role role;

    @NotNull(message = "Status is required.")
    private UserStatus status;
}
