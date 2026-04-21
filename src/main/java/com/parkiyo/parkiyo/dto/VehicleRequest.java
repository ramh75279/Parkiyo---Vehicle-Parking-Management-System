package com.parkiyo.parkiyo.dto;

import com.parkiyo.parkiyo.enums.VehicleCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VehicleRequest {
    private String licensePlate;
    private VehicleCategory category;
    private String make;
    private String model;
    private String color;
    private Integer year;
    private Long userId;
}
