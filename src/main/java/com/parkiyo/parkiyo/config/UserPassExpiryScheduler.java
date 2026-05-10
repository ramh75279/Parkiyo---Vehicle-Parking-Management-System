package com.parkiyo.parkiyo.config;

import com.parkiyo.parkiyo.service.UserPassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPassExpiryScheduler {

    private static final ZoneId COLOMBO = ZoneId.of("Asia/Colombo");

    private final UserPassService userPassService;

    /** Marks passes as EXPIRED after their last valid calendar day. */
    @Scheduled(cron = "0 5 0 * * *", zone = "Asia/Colombo")
    public void expirePasses() {
        LocalDate today = LocalDate.now(COLOMBO);
        int n = userPassService.expirePassesEndedBefore(today);
        if (n > 0) {
            log.info("Expired {} parking pass(es) before {}", n, today);
        }
    }
}
