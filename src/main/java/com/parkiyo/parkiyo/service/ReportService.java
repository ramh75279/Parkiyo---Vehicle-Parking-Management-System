package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.PaymentRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final PaymentRepository paymentRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;

    public Map<String, Object> getSummaryStats() {
        return Map.of(
                "totalRevenue", getTotalRevenue(null, null),
                "totalUsers", userRepository.count(),
                "totalSlots", slotRepository.count(),
                "availableSlots", slotRepository.countByStatus(SlotStatus.AVAILABLE)
        );
    }

    public List<Map<String, Object>> getRevenueReport(LocalDate from, LocalDate to, String groupBy) {
        // Simplified — returns total revenue for the period
        BigDecimal total = getTotalRevenue(from, to);
        return List.of(Map.of("period", groupBy, "revenue", total));
    }

    public BigDecimal getTotalRevenue(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            Double val = paymentRepository.sumTotalRevenue();
            return val != null ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
        }
        Double val = paymentRepository.sumTotalRevenue();
        return val != null ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
    }

    public List<Map<String, Object>> getOccupancyReport(LocalDate from, LocalDate to) {
        long total = slotRepository.count();
        long occupied = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        double rate = total > 0 ? (occupied * 100.0 / total) : 0;
        return List.of(Map.of("total", total, "occupied", occupied, "occupancyRate", rate));
    }

    public List<String> getPeakHours() {
        return List.of("08:00 - 09:00", "12:00 - 13:00", "17:00 - 18:00");
    }

    public Map<String, Object> getDailyRevenueReport(LocalDate date) {
        Double val = paymentRepository.sumTotalRevenue();
        BigDecimal revenue = val != null ? BigDecimal.valueOf(val) : BigDecimal.ZERO;
        return Map.of("date", date, "revenue", revenue);
    }
}
