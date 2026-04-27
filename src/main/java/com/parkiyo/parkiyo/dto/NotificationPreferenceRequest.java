package com.parkiyo.parkiyo.dto;

import lombok.Data;

@Data
public class NotificationPreferenceRequest {
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
}