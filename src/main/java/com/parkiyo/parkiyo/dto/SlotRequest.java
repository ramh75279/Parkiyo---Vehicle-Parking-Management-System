package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class SlotRequest {
    private String slotNumber;
    private String zone;
    private String floor;
    private BigDecimal hourlyRate;
    private String description;
    private boolean active = true;
}