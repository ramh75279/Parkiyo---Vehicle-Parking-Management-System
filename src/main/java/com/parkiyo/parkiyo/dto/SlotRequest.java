package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class SlotRequest {
    private String slotNumber;
    private String zone;
    private BigDecimal hourlyRate;
}
