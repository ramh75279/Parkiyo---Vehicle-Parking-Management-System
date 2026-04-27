package com.parkiyo.parkiyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ParkiyoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkiyoApplication.class, args);
    }
}