package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.DashboardStats;
import com.parkiyo.parkiyo.dto.LiveOperationsRow;
import com.parkiyo.parkiyo.dto.SlotOccupancySummary;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.Notification;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Vehicle;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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
        return parkingRecordRepository.findRecentEntriesForDashboard(
                PageRequest.of(0, Math.max(1, limit)));
    }

    /**
     * Live Operations: recent parking sessions plus vehicles that are registered but have never entered
     * (no {@link ParkingRecord} yet), so newly added vehicles appear immediately.
     */
    public List<LiveOperationsRow> getAdminLiveOperationsRows(String rawQuery) {
        List<ParkingRecord> pool = parkingRecordRepository.findRecentEntriesForDashboard(
                PageRequest.of(0, 500));
        List<ParkingRecord> recordsFiltered;
        if (rawQuery == null || rawQuery.isBlank()) {
            recordsFiltered = pool.stream().limit(200).toList();
        } else {
            String q = rawQuery.trim().toLowerCase(Locale.ROOT);
            String qc = compactSearchKey(q);
            recordsFiltered = pool.stream()
                    .filter(pr -> matchesLiveOpsQuery(pr, q, qc))
                    .limit(200)
                    .toList();
        }

        java.util.Set<String> platesInSessions = recordsFiltered.stream()
                .map(pr -> pr.getVehicle().getLicensePlate())
                .collect(Collectors.toSet());

        List<Vehicle> neverParked = vehicleRepository.findWithoutParkingHistory(PageRequest.of(0, 120));
        List<LiveOperationsRow> registrationRows = new ArrayList<>();
        for (Vehicle v : neverParked) {
            if (platesInSessions.contains(v.getLicensePlate())) {
                continue;
            }
            if (rawQuery != null && !rawQuery.isBlank()) {
                String q = rawQuery.trim().toLowerCase(Locale.ROOT);
                String qc = compactSearchKey(q);
                if (!matchesRegisteredVehicleQuery(v, q, qc)) {
                    continue;
                }
            }
            registrationRows.add(LiveOperationsRow.registeredVehicle(v));
            if (registrationRows.size() >= 40) {
                break;
            }
        }

        List<LiveOperationsRow> merged = new ArrayList<>(registrationRows.size() + recordsFiltered.size());
        merged.addAll(registrationRows);
        for (ParkingRecord pr : recordsFiltered) {
            merged.add(LiveOperationsRow.parkingSession(pr));
        }
        return merged;
    }

    private static boolean matchesLiveOpsQuery(ParkingRecord pr, String qLower, String qCompact) {
        Vehicle v = pr.getVehicle();
        String plate = v != null && v.getLicensePlate() != null ? v.getLicensePlate() : "";
        ParkingSlot slot = pr.getSlot();
        String bay = slot != null && slot.getSlotNumber() != null ? slot.getSlotNumber() : "";

        User owner = pr.getUser();
        if (owner == null && v != null) {
            owner = v.getUser();
        }
        String userHay;
        if (owner != null) {
            userHay = (owner.getFirstName() + " " + owner.getLastName() + " " + owner.getEmail()).trim();
        } else {
            userHay = "guest visitor walk-in";
        }

        String sessionStatus = pr.isActive() ? "active inside parked" : "exited completed left";
        String paymentStatus = pr.getPayment() == null ? "pending unpaid owing" : "settled paid complete";

        String hay = (plate + " license plate "
                + "bay slot parking space " + bay + " "
                + userHay + " username email "
                + sessionStatus + " "
                + paymentStatus).toLowerCase(Locale.ROOT);

        if (hay.contains(qLower)) {
            return true;
        }
        return !qCompact.isEmpty() && compactSearchKey(hay).contains(qCompact);
    }

    private static String compactSearchKey(String s) {
        return s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9@._]", "");
    }

    private static boolean matchesRegisteredVehicleQuery(Vehicle v, String qLower, String qCompact) {
        String plate = v.getLicensePlate() != null ? v.getLicensePlate() : "";
        User owner = v.getUser();
        String userHay;
        if (owner != null) {
            userHay = (owner.getFirstName() + " " + owner.getLastName() + " " + owner.getEmail()).trim();
        } else {
            userHay = "guest visitor walk-in";
        }
        String hay = (plate + " license plate "
                + userHay + " username email "
                + "registered new vehicle not entered pending entry gate").toLowerCase(Locale.ROOT);
        if (hay.contains(qLower)) {
            return true;
        }
        return !qCompact.isEmpty() && compactSearchKey(hay).contains(qCompact);
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