package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.DashboardStats;
import com.parkiyo.parkiyo.dto.SlotOccupancySummary;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.Notification;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.repository.NotificationRepository;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ParkingRecordRepository parkingRecordRepository;
    private final NotificationRepository notificationRepository;

    public DashboardStats getAdminDashboardStats() {
        long totalSlots = slotRepository.count();
        long occupiedSlots = slotRepository.countByStatusOccupied();
        long availableSlots = totalSlots - occupiedSlots;

        long totalVehicles = vehicleRepository.count();
        long totalUsers = userRepository.count();
        long activeRecords = parkingRecordRepository.countActiveRecords();

        BigDecimal todayRevenue = parkingRecordRepository.calculateTodayRevenue();

        return DashboardStats.builder()
                .totalSlots((int) totalSlots)
                .occupiedSlots((int) occupiedSlots)
                .availableSlots((int) availableSlots)
                .totalVehicles((int) totalVehicles)
                .totalUsers((int) totalUsers)
                .activeRecords((int) activeRecords)
                .totalRevenue(todayRevenue != null ? todayRevenue : BigDecimal.ZERO)
                .build();
    }

    public SlotOccupancySummary getSlotOccupancySummary() {
        long total = slotRepository.count();
        long occupied = slotRepository.countByStatusOccupied();
        double occupancyRate = total > 0 ? (occupied * 100.0 / total) : 0.0;

        int reserved = (int) slotRepository.countByStatus(SlotStatus.RESERVED);

        return SlotOccupancySummary.builder()
                .total((int) total)
                .occupied((int) occupied)
                .available((int) (total - occupied))
                .reserved(reserved)
                .occupancyRate(Math.round(occupancyRate * 10) / 10.0)
                .build();
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTopRecentEntries(limit);
    }

    public List<ParkingRecord> getRecentPayments(int limit) {
        return parkingRecordRepository.findRecentPaid(limit);
    }

    // Real notifications — fetches latest N across all users
    public List<Notification> getAdminRecentNotifications(int limit) {
        return notificationRepository.findTopByOrderByCreatedAtDesc(PageRequest.of(0, limit));
    }

    // Real unread count — counts all unread notifications in the system
    public long getAdminUnreadNotificationCount() {
        return notificationRepository.countByReadFalse();
    }

    // ================== USER DASHBOARD METHODS ==================

    public List<ParkingRecord> getUserRecentParking(String email, int limit) {
        return parkingRecordRepository.findByUserEmailRecent(email, limit);
    }

    public ParkingRecord getUserActiveReservation(String email) {
        return parkingRecordRepository.findActiveByUserEmail(email).orElse(null);
    }

    public List<ParkingRecord> getUserPendingPayments(String email) {
        return parkingRecordRepository.findPendingPaymentsByUser(email);
    }

    public long countUserEntriesToday(String email) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return parkingRecordRepository.countUserEntriesBetween(email, start, end);
    }

    public long countUserExitsToday(String email) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return parkingRecordRepository.countUserExitsBetween(email, start, end);
    }

    /** Distinct sessions the user started or ended today (for dashboard throughput). */
    public long countUserParkingTouchToday(String email) {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return parkingRecordRepository.countUserParkingTouchBetween(email, start, end);
    }
}