package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationPreferenceRequest {
    private boolean emailNotificationsEnabled;
    private boolean smsNotificationsEnabled;
}
