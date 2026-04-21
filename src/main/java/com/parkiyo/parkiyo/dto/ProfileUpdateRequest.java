package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phone;
}
