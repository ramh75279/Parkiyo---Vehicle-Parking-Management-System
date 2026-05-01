package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExitRequest {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9\\s-]{5,15}$", message = "Invalid Sri Lankan license plate format")
    private String licensePlate;

    // Optional: Can be used by admin if they want to select record manually
    private Long parkingRecordId;
}