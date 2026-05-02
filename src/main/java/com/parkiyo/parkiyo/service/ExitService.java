package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

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
    public Long processExit(ExitRequest request, String operatorEmail) {
        String normalizedPlate = normalizeLicensePlate(request.getLicensePlate());

        ParkingRecord record = parkingRecordRepository
                .findByVehicleLicensePlateAndActiveTrue(normalizedPlate)
                .orElseThrow(() -> new RuntimeException("No active parking record found for vehicle: " + normalizedPlate));

        LocalDateTime exitTime = LocalDateTime.now(COLOMBO_ZONE);
        Duration duration = Duration.between(record.getEntryTime(), exitTime);
        long minutes = Math.max(duration.toMinutes(), 1);

        BigDecimal amount = calculateParkingFee(minutes, record.getSlot());

        // Update Parking Record
        record.setExitTime(exitTime);
        record.setDurationMinutes((int) minutes);
        record.setAmountCharged(amount);
        record.setExitOperator(operatorEmail);
        record.setActive(false);
        parkingRecordRepository.save(record);

        // Free the slot
        ParkingSlot slot = record.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        // Create Payment
        Payment payment = Payment.builder()
                .transactionCode("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(record.getUser())
                .parkingRecord(record)
                .amount(amount)
                .paymentMethod("CASH")
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        // Notifications & Audit
        notificationService.createNotification(
                record.getUser(),
                NotificationType.EXIT,
                "Vehicle Exit Processed",
                "Vehicle " + normalizedPlate + " exited. Amount due: Rs. " + amount,
                "/payments/pending/" + payment.getId()
        );

        auditLogService.logAction(
                "EXIT",
                operatorEmail,
                "ParkingRecord",
                record.getId(),
                "Vehicle " + normalizedPlate + " exited from slot " + slot.getSlotNumber() + ". Fee: Rs." + amount,
                null, null, null
        );

        return payment.getId();
    }

    private String normalizeLicensePlate(String plate) {
        if (plate == null) return "";
        return plate.toUpperCase().replaceAll("\\s+", "").replace("-", "");
    }

    private BigDecimal calculateParkingFee(long minutes, ParkingSlot slot) {
        BigDecimal hourlyRate = slot.getHourlyRate() != null ? slot.getHourlyRate() : BigDecimal.valueOf(200);

        if (minutes <= 15) return BigDecimal.ZERO;

        long billableHours = (long) Math.ceil((minutes - 15) / 60.0);
        return hourlyRate.multiply(BigDecimal.valueOf(Math.max(billableHours, 1)));
    }

    public List<ParkingRecord> getRecentExits(int limit) {
        return parkingRecordRepository.findTop20ByOrderByEntryTimeDesc()
                .stream()
                .filter(r -> !r.isActive())
                .limit(limit)
                .toList();
    }
}