package com.parkiyo.parkiyo.enums;

public enum FeedbackReportStatus {
    PENDING,
    IN_REVIEW,
    RESOLVED,
    REJECTED;

    public String getDisplayLabel() {
        return switch (this) {
            case PENDING -> "Pending";
            case IN_REVIEW -> "In review";
            case RESOLVED -> "Resolved";
            case REJECTED -> "Rejected";
        };
    }
}
