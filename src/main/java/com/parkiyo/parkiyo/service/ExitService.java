package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.exception.BadRequestException;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ExitService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingSlotRepository slotRepository;
    private final PaymentRepository paymentRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    private static final ZoneId COLOMBO_ZONE = ZoneId.of("Asia/Colombo");

    @Transactional
    public void processExit(ExitRequest request, String operatorEmail) {
        if (request.getLicensePlate() == null || request.getLicensePlate().trim().isEmpty()) {
            throw new BadRequestException("License plate is required");
        }

        String normalizedPlate = normalizeLicensePlate(request.getLicensePlate());

        ParkingRecord record = parkingRecordRepository
                .findByVehicleLicensePlateAndActiveTrue(normalizedPlate)
                .orElseThrow(() -> new BadRequestException("No active parking record found for vehicle: " + normalizedPlate));

        LocalDateTime exitTime = LocalDateTime.now(COLOMBO_ZONE);
        record.setExitTime(exitTime);
        record.setActive(false);

        long durationMinutes = ChronoUnit.MINUTES.between(record.getEntryTime(), exitTime);
        BigDecimal fee = calculateBasicFee(durationMinutes);

        parkingRecordRepository.save(record);

        // Free up the slot
        ParkingSlot slot = record.getSlot();
        if (slot != null) {
            slot.setStatus(SlotStatus.AVAILABLE);
            slotRepository.save(slot);
        }

        // Create Payment
        Payment payment = Payment.builder()
                .parkingRecord(record)
                .amount(fee)
                .status(PaymentStatus.PENDING)
                .paymentMethod("CASH")                    // Default for now (since DTO doesn't have it)
                .build();

        paymentRepository.save(payment);

        // Audit Log
        auditLogService.logAction(
                "VEHICLE_EXIT",
                operatorEmail,
                "ParkingRecord",
                record.getId(),
                "Vehicle " + normalizedPlate + " exited. Fee: " + fee + " LKR",
                null,
                null,
                null
        );

        // Notification
        if (record.getUser() != null) {
            notificationService.createNotification(
                    record.getUser(),
                    NotificationType.EXIT,
                    "Vehicle Exit",
                    "Your vehicle " + normalizedPlate + " exited. Total fee: " + fee + " LKR",
                    "/payments"
            );
        }
    }

    private BigDecimal calculateBasicFee(long minutes) {
        if (minutes <= 30) return BigDecimal.valueOf(50);
        if (minutes <= 60) return BigDecimal.valueOf(100);
        if (minutes <= 120) return BigDecimal.valueOf(200);
        return BigDecimal.valueOf(200 + (minutes - 120) * 2);
    }

    private String normalizeLicensePlate(String plate) {
        if (plate == null) return "";
        return plate.trim()
                .toUpperCase()
                .replaceAll("\\s+", "")
                .replace("-", "")
                .replace(".", "");
    }
}