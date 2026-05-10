package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.VehicleCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VehicleRequest {
    @NotNull(message = "License plate is required")
    @NotBlank(message = "License plate cannot be blank")
    private String licensePlate;
    
    @NotNull(message = "Vehicle category is required")
    private VehicleCategory category;
    private String make;
    private String model;
    private String color;
    private Integer year;
    private Long userId;
}
