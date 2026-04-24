package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String phone;

    private MultipartFile profilePicture;   // ←←← NEW
}