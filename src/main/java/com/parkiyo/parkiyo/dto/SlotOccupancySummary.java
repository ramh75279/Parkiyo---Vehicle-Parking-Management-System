package com.parkiyo.parkiyo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotOccupancySummary {
    private int total;
    private int occupied;
    private int available;
    /** Slots in RESERVED status */
    private int reserved;
    private double occupancyRate;
}