package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.ReservationRequest;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.*;
import com.parkiyo.parkiyo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public List<Reservation> getUserReservations(String email) {
        return reservationRepository.findByUserEmail(email);
    }

    public Reservation getActiveReservation(String email) {
        return reservationRepository
                .findTopByUserEmailAndStatusOrderByCreatedAtDesc(email, ReservationStatus.CONFIRMED)
                .orElse(null);
    }

    @Transactional
    public Long createReservation(ReservationRequest request, String email) {
        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found."));
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotNumber() + " is not available.");
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Calculate fee
        long hours = (long) Math.ceil(
                Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0);
        BigDecimal amount = slot.getHourlyRate().multiply(BigDecimal.valueOf(hours));

        Reservation reservation = Reservation.builder()
                .reservationCode("RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .slot(slot)
                .vehicle(vehicle)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(ReservationStatus.CONFIRMED)
                .build();
        reservationRepository.save(reservation);

        // Reserve the slot
        slot.setStatus(SlotStatus.RESERVED);
        slotRepository.save(slot);

        // Create a pending payment
        Payment payment = Payment.builder()
                .transactionCode("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .reservation(reservation)
                .amount(amount)
                .paymentMethod("Pending")
                .status(PaymentStatus.PENDING)
                .build();
        paymentRepository.save(payment);

        return payment.getId();
    }

    @Transactional
    public void cancelReservation(Long id, String email) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));
        if (!reservation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not authorised to cancel this reservation.");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Free the slot
        ParkingSlot slot = reservation.getSlot();
        slot.setStatus(SlotStatus.AVAILABLE);
        slotRepository.save(slot);
    }
}
