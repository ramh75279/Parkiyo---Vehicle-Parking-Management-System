package com.parkiyo.parkiyo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SavedReportType {
    REVENUE("Revenue"),
    OCCUPANCY("Occupancy"),
    DAILY_REVENUE("Daily revenue");

    private final String label;

    /** URL segment under {@code /admin/reports/…} for the live analytics view. */
    public String pathSegment() {
        return switch (this) {
            case REVENUE -> "revenue";
            case OCCUPANCY -> "occupancy";
            case DAILY_REVENUE -> "daily-revenue";
        };
    }
}
