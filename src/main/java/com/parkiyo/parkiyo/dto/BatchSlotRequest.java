package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter
public class BatchSlotRequest {
    private String prefix;
    private String zone;
    private int count;
    private int startFrom;
    private BigDecimal hourlyRate;
}
