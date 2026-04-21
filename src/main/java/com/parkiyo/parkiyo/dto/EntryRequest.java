package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EntryRequest {
    private String licensePlate;
    private Long slotId;
    private String category;
}
