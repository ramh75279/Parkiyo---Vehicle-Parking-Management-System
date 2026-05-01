package com.parkiyo.parkiyo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryRequest {

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9\\s-]{5,15}$", message = "Invalid Sri Lankan license plate format")
    private String licensePlate;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;     // CAR, BIKE, VAN, THREE_WHEELER, BUS, etc.

    // Optional - We will mostly auto-assign slot
    private Long slotId;
}