package com.parkiyo.parkiyo.enums;

public enum FeedbackPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public String getDisplayLabel() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
