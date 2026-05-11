package com.parkiyo.parkiyo.enums;

/**
 * Categories for customer feedback and incident reports.
 */
public enum FeedbackCategory {
    COMPLAINT,
    SUGGESTION,
    REVIEW,
    TECHNICAL_ISSUE,
    SECURITY_ISSUE,
    CLEANLINESS,
    PARKING_AVAILABILITY;

    public String getDisplayLabel() {
        return switch (this) {
            case COMPLAINT -> "Complaint";
            case SUGGESTION -> "Suggestion";
            case REVIEW -> "Review / Rating";
            case TECHNICAL_ISSUE -> "Technical issue";
            case SECURITY_ISSUE -> "Security issue";
            case CLEANLINESS -> "Cleanliness";
            case PARKING_AVAILABILITY -> "Parking availability";
        };
    }
}
