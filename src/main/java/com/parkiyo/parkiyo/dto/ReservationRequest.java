package com.parkiyo.parkiyo.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationRequest {
    private Long slotId;
    private Long vehicleId;

    /** When no {@code vehicleId}: plate from the reservation flow + category (find or create vehicle). */
    private String reservationPlate;
    private String reservationVehicleCategory;

    /** Matches HTML {@code datetime-local} (with or without seconds). */
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
