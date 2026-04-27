package com.parkiyo.parkiyo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private int totalSlots;
    private int occupiedSlots;
    private int availableSlots;
    private int totalVehicles;
    private int totalUsers;
    private int activeRecords;
    private BigDecimal totalRevenue;
}