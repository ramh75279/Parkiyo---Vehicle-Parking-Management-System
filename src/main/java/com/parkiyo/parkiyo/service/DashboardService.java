package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.Notification;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;

    // ─── Admin dashboard ────────────────────────────────────────────────────

    public Map<String, Object> getAdminDashboardStats() {
        return Map.of(
                "totalUsers", userRepository.count(),
                "totalSlots", slotRepository.count(),
                "availableSlots", slotRepository.countByStatus(SlotStatus.AVAILABLE),
                "occupiedSlots", slotRepository.countByStatus(SlotStatus.OCCUPIED),
                "activeRecords", parkingRecordRepository.findByActiveTrue().size(),
                "totalRevenue", paymentRepository.sumTotalRevenue() != null
                        ? paymentRepository.sumTotalRevenue() : 0.0
        );
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTop20ByOrderByCreatedAtDesc()
                .stream().limit(limit).toList();
    }

    public List<Payment> getRecentPayments(int limit) {
        return paymentRepository.findTop10ByOrderByCreatedAtDesc()
                .stream().limit(limit).toList();
    }

    public Map<String, Object> getSlotOccupancySummary() {
        long total = slotRepository.count();
        long available = slotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupied = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        return Map.of(
                "total", total,
                "available", available,
                "occupied", occupied,
                "occupancyRate", total > 0 ? (occupied * 100.0 / total) : 0
        );
    }

    public Map<String, Object> getSystemHealth() {
        return Map.of(
                "status", "OK",
                "totalUsers", userRepository.count(),
                "totalSlots", slotRepository.count(),
                "activeRecords", parkingRecordRepository.findByActiveTrue().size()
        );
    }

    public List<Notification> getAdminNotifications() {
        return notificationRepository.findAll().stream()
                .sorted((a, b) -> {
                    if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                    if (a.getCreatedAt() == null) return 1;
                    if (b.getCreatedAt() == null) return -1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .toList();
    }

    // ─── User dashboard ─────────────────────────────────────────────────────

    public List<ParkingRecord> getUserRecentParking(String email, int limit) {
        return parkingRecordRepository.findByUserEmail(email)
                .stream().limit(limit).toList();
    }

    public Reservation getUserActiveReservation(String email) {
        return reservationRepository
                .findTopByUserEmailAndStatusOrderByCreatedAtDesc(email, ReservationStatus.CONFIRMED)
                .orElse(null);
    }

    public List<Payment> getUserPendingPayments(String email) {
        return paymentRepository.findByUserEmailAndStatus(email, PaymentStatus.PENDING);
    }
}
