package com.parkiyo.parkiyo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "parkiyo.bootstrap")
public class ParkiyoBootstrapProperties {

    private boolean enabled = true;
    private boolean demoSlotsEnabled = false;

    private String adminEmail = "admin@parkiyo.com";
    private String adminPassword = "Admin@12345";
    private String adminFirstName = "Parkiyo";
    private String adminLastName = "Admin";
}