package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class ReservationRequest {
    private Long slotId;
    private Long vehicleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
