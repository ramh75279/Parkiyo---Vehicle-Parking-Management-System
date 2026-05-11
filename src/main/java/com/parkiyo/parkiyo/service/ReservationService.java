package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.ReservationRequest;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.*;
import com.parkiyo.parkiyo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final VehicleService vehicleService;

    /**
     * Get all reservations for a user - NEVER returns null
     */
    @Transactional(readOnly = true)
    public List<Reservation> getUserReservations(String email) {
        if (email == null || email.isBlank()) {
            return Collections.emptyList();
        }
        List<Reservation> reservations = reservationRepository.findAllByUserEmailWithAssociations(email);
        return reservations != null ? reservations : Collections.emptyList();
    }

    /** Confirmed reservations whose start time falls in the next 7 calendar days (including today). */
    @Transactional(readOnly = true)
    public long countConfirmedStartingWithinSevenDays(String email) {
        if (email == null || email.isBlank()) {
            return 0;
        }
        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime until = LocalDate.now().plusDays(7).atStartOfDay();
        return reservationRepository.countByUserEmailAndStatusAndStartTimeBetween(
                email, ReservationStatus.CONFIRMED, from, until);
    }

    /** Reservations with a start time in the current calendar month (excluding cancelled). */
    @Transactional(readOnly = true)
    public long countBookingsStartingThisMonth(String email) {
        if (email == null || email.isBlank()) {
            return 0;
        }
        YearMonth ym = YearMonth.now();
        LocalDateTime from = ym.atDay(1).atStartOfDay();
        LocalDateTime until = ym.plusMonths(1).atDay(1).atStartOfDay();
        return reservationRepository.countByUserEmailAndStartTimeBetweenExcludingStatus(
                email, from, until, ReservationStatus.CANCELLED);
    }

    /** Cancelled reservations whose updated timestamp falls in the current calendar month. */
    @Transactional(readOnly = true)
    public long countCancellationsThisMonth(String email) {
        if (email == null || email.isBlank()) {
            return 0;
        }
        YearMonth ym = YearMonth.now();
        LocalDateTime from = ym.atDay(1).atStartOfDay();
        LocalDateTime until = ym.plusMonths(1).atDay(1).atStartOfDay();
        return reservationRepository.countByUserEmailAndStatusAndUpdatedAtBetween(
                email, ReservationStatus.CANCELLED, from, until);
    }

    /**
     * Get active (CONFIRMED) reservation for user
     */
    @Transactional(readOnly = true)
    public Reservation getActiveReservation(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        return reservationRepository
                .findTopByUserEmailAndStatusOrderByCreatedAtDesc(email, ReservationStatus.CONFIRMED)
                .orElse(null);
    }

    /**
     * Fetches a single reservation by ID with ownership verification
     */
    public Reservation getReservationByIdForUser(Long id, String email) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));

        if (!reservation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not authorised to access this reservation.");
        }
        return reservation;
    }

    // ====================== COUNT METHODS FOR DASHBOARD/STATS ======================

    /**
     * Count upcoming (CONFIRMED) reservations
     */
    @Transactional(readOnly = true)
    public long countUpcomingReservations(String email) {
        if (email == null || email.isBlank()) return 0;
        return reservationRepository.countByUserEmailAndStatus(email, ReservationStatus.CONFIRMED);
    }

    /**
     * Count reservations scheduled for today
     */
    @Transactional(readOnly = true)
    public long countTodayReservations(String email) {
        if (email == null || email.isBlank()) return 0;
        LocalDate today = LocalDate.now();
        return reservationRepository.countByUserEmailAndStatusAndStartTimeToday(email, ReservationStatus.CONFIRMED, today);
    }

    /**
     * Count cancelled reservations
     */
    @Transactional(readOnly = true)
    public long countCancelledReservations(String email) {
        if (email == null || email.isBlank()) return 0;
        return reservationRepository.countByUserEmailAndStatus(email, ReservationStatus.CANCELLED);
    }

    /**
     * Count completed reservations
     */
    @Transactional(readOnly = true)
    public long countCompletedReservations(String email) {
        if (email == null || email.isBlank()) return 0;
        return reservationRepository.countByUserEmailAndStatus(email, ReservationStatus.COMPLETED);
    }

    // ====================== CORE BUSINESS METHODS ======================

    @Transactional
    public Long createReservation(ReservationRequest request, String email) {
        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found."));

        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotNumber() + " is not available.");
        }

        Vehicle vehicle = resolveVehicleForReservation(request, email);

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

        // Create pending payment
        Payment payment = Payment.builder()
                .transactionCode("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .reservation(reservation)
                .amount(amount)
                .paymentMethod("Pending")
                .status(PaymentStatus.PENDING)
                .build();

        paymentRepository.save(payment);

        notificationService.createNotification(
                user,
                NotificationType.RESERVATION,
                "Reservation Confirmed",
                "Reservation " + reservation.getReservationCode() + " was created for slot " + slot.getSlotNumber() + ".",
                "/reservations"
        );

        return payment.getId();
    }

    @Transactional
    public void updateReservation(Long id, ReservationRequest request, String email) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found."));

        if (!reservation.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Not authorised to edit this reservation.");
        }
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Only upcoming reservations can be edited.");
        }

        ParkingSlot oldSlot = reservation.getSlot();
        ParkingSlot newSlot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found."));

        // Handle slot change
        if (!oldSlot.getId().equals(newSlot.getId())) {
            if (newSlot.getStatus() != SlotStatus.AVAILABLE) {
                throw new RuntimeException("Slot " + newSlot.getSlotNumber() + " is not available.");
            }
            oldSlot.setStatus(SlotStatus.AVAILABLE);
            slotRepository.save(oldSlot);

            newSlot.setStatus(SlotStatus.RESERVED);
            slotRepository.save(newSlot);

            reservation.setSlot(newSlot);
        }

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));

        reservation.setVehicle(vehicle);
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservationRepository.save(reservation);

        // Recalculate payment amount
        long hours = (long) Math.ceil(
                Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0);
        BigDecimal newAmount = newSlot.getHourlyRate().multiply(BigDecimal.valueOf(hours));

        Payment payment = reservation.getPayment();
        if (payment != null && payment.getStatus() == PaymentStatus.PENDING) {
            payment.setAmount(newAmount);
            paymentRepository.save(payment);
        }

        notificationService.createNotification(
                reservation.getUser(),
                NotificationType.RESERVATION,
                "Reservation Updated",
                "Reservation " + reservation.getReservationCode() + " has been updated.",
                "/reservations"
        );
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

        notificationService.createNotification(
                reservation.getUser(),
                NotificationType.RESERVATION,
                "Reservation Cancelled",
                "Reservation " + reservation.getReservationCode() + " was cancelled.",
                "/reservations"
        );
    }

    private Vehicle resolveVehicleForReservation(ReservationRequest request, String email) {
        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found."));
            if (vehicle.getUser() != null && !email.equalsIgnoreCase(vehicle.getUser().getEmail())) {
                throw new RuntimeException("Vehicle does not belong to your account.");
            }
            return vehicle;
        }
        String plate = request.getReservationPlate();
        String catRaw = request.getReservationVehicleCategory();
        if (plate != null && !plate.isBlank() && catRaw != null && !catRaw.isBlank()) {
            try {
                VehicleCategory cat = VehicleCategory.valueOf(catRaw.trim().toUpperCase(Locale.ROOT));
                return vehicleService.getOrCreateVehicleForUserReservation(email, plate.trim(), cat);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid vehicle category.");
            }
        }
        throw new RuntimeException("Vehicle is required.");
    }
}