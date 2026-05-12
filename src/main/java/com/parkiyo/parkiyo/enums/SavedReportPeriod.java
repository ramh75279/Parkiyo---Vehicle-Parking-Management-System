package com.parkiyo.parkiyo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** How often the bookmarked analytics view should be read or aggregated (separate from {@link SavedReportType}). */
@Getter
@RequiredArgsConstructor
public enum SavedReportPeriod {
    DAILY("Daily"),
    MONTHLY("Monthly"),
    YEARLY("Yearly");

    private final String label;
}
