package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryResponse {

    private String licensePlate;
    private String vehicleType;
    private String message;
    private String status;
    private Long assignedSlotId;
    private String ticketNumber;
}