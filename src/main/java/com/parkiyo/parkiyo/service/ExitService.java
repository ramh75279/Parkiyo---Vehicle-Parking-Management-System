package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.ExitRequest;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExitService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingSlotRepository slotRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public Long processExit(ExitRequest request, String operatorEmail) {
        ParkingRecord record = parkingRecordRepository.findById(request.getParkingRecordId())
                .orElseThrow(() -> new RuntimeException("Parking record not found."));

        if (!record.isActive()) {
            throw new RuntimeException("This vehicle has already exited.");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Duration.between(record.getEntryTime(), exitTime).toMinutes();
        if (minutes < 1) minutes = 1;

        // Calculate fee based on slot hourly rate
        ParkingSlot slot = record.getSlot();
        BigDecimal hours = BigDecimal.valueOf(Math.ceil(minutes / 60.0));
        BigDecimal amount = slot.getHourlyRate().multiply(hours);

        // Update record
        record.setExitTime(exitTime);
        record.setDurationMinutes((int) minutes);
        record.setAmountCharged(amount);
        record.setExitOperator(operatorEmail);
        record.setActive(false);
        parkingRecordRepository.save(record);

        // Free up the slot
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);

        // Create a pending payment
        Payment payment = Payment.builder()
                .transactionCode("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(record.getUser())
                .parkingRecord(record)
                .amount(amount)
                .paymentMethod("Pending")
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        return payment.getId();
    }

    public List<ParkingRecord> getRecentExits(int limit) {
        return parkingRecordRepository.findAll().stream()
                .filter(r -> !r.isActive())
            .sorted(Comparator.comparing(
                ParkingRecord::getExitTime,
                Comparator.nullsLast(Comparator.reverseOrder())
            ))
                .limit(limit)
                .toList();
    }
}
