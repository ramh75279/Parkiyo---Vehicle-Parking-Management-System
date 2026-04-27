package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.DashboardStats;
import com.parkiyo.parkiyo.dto.SlotOccupancySummary;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    public DashboardStats getAdminDashboardStats() {
        long totalSlots = slotRepository.count();
        long occupiedSlots = slotRepository.countByStatusOccupied();  // Fixed
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
        long occupied = slotRepository.countByStatusOccupied();   // Fixed
        double occupancyRate = total > 0 ? (occupied * 100.0 / total) : 0.0;

        return SlotOccupancySummary.builder()
                .total((int) total)
                .occupied((int) occupied)
                .available((int) (total - occupied))
                .occupancyRate(Math.round(occupancyRate * 10) / 10.0)
                .build();
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTopRecentEntries(limit);   // Fixed
    }

    public List<ParkingRecord> getRecentPayments(int limit) {
        return parkingRecordRepository.findRecentPaid(limit);         // Fixed
    }

    public List<Object> getAdminRecentNotifications(int limit) {
        // TODO: Replace with real Notification entity later
        return List.of();
    }

    public long getAdminUnreadNotificationCount() {
        return 3; // Temporary
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
}