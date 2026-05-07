package com.parkiyo.parkiyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan("com.parkiyo.parkiyo.model")
@EnableJpaRepositories("com.parkiyo.parkiyo.repository")
@EnableAsync
public class ParkiyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkiyoApplication.class, args);
    }
}