package com.parkiyo.parkiyo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "parkiyo.bootstrap")
public class ParkiyoBootstrapProperties {

    private boolean enabled = true;
    private String adminEmail = "admin@parkiyo.com";
    private String adminPassword = "Admin@12345";
    private String adminFirstName = "Parkiyo";
    private String adminLastName = "Admin";
    private boolean demoSlotsEnabled = true;
}
