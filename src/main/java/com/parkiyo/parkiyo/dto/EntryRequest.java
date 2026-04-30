package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryRequest {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9\\s-]{3,15}$", message = "Invalid license plate format")
    private String licensePlate;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;        // CAR, BIKE, VAN, THREE_WHEELER, etc.

    // Optional: If you want to support pre-booked or VIP slots in future
    private Long preferredSlotId;

    // Can be added later: driver contact, membership ID, etc.
}