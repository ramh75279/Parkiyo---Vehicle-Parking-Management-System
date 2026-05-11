package com.parkiyo.parkiyo.enums;

public enum NotificationType {
    ENTRY,
    EXIT,
    PAYMENT,
    RESERVATION,
    SYSTEM;

    /** UI filter bucket: {@code all} | {@code unread} | {@code payment} | {@code system} | {@code alert} */
    public String uiCategory() {
        return switch (this) {
            case PAYMENT -> "payment";
            case SYSTEM -> "system";
            case ENTRY, EXIT, RESERVATION -> "alert";
        };
    }
}
