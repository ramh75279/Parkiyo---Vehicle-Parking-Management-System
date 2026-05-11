package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.BatchSlotRequest;
import com.parkiyo.parkiyo.dto.SlotRequest;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final ParkingSlotRepository slotRepository;
    private final ParkingRecordRepository parkingRecordRepository;
    private final AuditLogService auditLogService;

    public List<ParkingSlot> getAvailableSlots() {
        return slotRepository.findByStatus(SlotStatus.AVAILABLE);
    }

    public List<ParkingSlot> getAvailableSlots(LocalDate date, String zone) {
        List<ParkingSlot> slots = slotRepository.findByStatus(SlotStatus.AVAILABLE);
        if (zone != null && !zone.isBlank()) {
            slots = slots.stream().filter(s -> zone.equalsIgnoreCase(s.getZone())).toList();
        }
        return slots;
    }

    /** All slots (every status), ordered for the user slot-selection map. */
    public List<ParkingSlot> findAllSlotsForSlotSelection() {
        List<ParkingSlot> list = new ArrayList<>(slotRepository.findAll());
        list.sort(Comparator
                .comparing((ParkingSlot s) -> s.getZone() != null ? s.getZone() : "", String.CASE_INSENSITIVE_ORDER)
                .thenComparing(s -> s.getSlotNumber() != null ? s.getSlotNumber() : "", String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    public List<ParkingSlot> getSlots(String status, String zone) {
        List<ParkingSlot> slots = slotRepository.findAll();
        if (status != null && !status.isBlank()) {
            SlotStatus s = SlotStatus.valueOf(status.toUpperCase());
            slots = slots.stream().filter(slot -> slot.getStatus() == s).toList();
        }
        if (zone != null && !zone.isBlank()) {
            slots = slots.stream().filter(slot -> zone.equalsIgnoreCase(slot.getZone())).toList();
        }
        return slots;
    }

    public ParkingSlot getSlotById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found: " + id));
    }

    public List<String> getAllZones() {
        return slotRepository.findAll().stream()
                .map(ParkingSlot::getZone)
                .filter(z -> z != null && !z.isBlank())
                .distinct()
                .sorted()
                .toList();
    }

    /** Average hourly rate across all slots — used for reservation modal estimates. */
    public BigDecimal getAverageHourlyRateForEstimate() {
        List<ParkingSlot> slots = slotRepository.findAll();
        if (slots.isEmpty()) {
            return new BigDecimal("5.50");
        }
        BigDecimal sum = slots.stream()
                .map(s -> s.getHourlyRate() != null ? s.getHourlyRate() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(slots.size()), 2, RoundingMode.HALF_UP);
    }

    public Map<String, Object> getSlotOverview() {
        long total = slotRepository.count();
        long available = slotRepository.countByStatus(SlotStatus.AVAILABLE);
        long occupied = slotRepository.countByStatus(SlotStatus.OCCUPIED);
        long reserved = slotRepository.countByStatus(SlotStatus.RESERVED);
        long maintenance = slotRepository.countByStatus(SlotStatus.MAINTENANCE);
        return Map.of(
                "total", total,
                "available", available,
                "occupied", occupied,
                "reserved", reserved,
                "maintenance", maintenance,
                "occupancyRate", total > 0 ? (occupied * 100.0 / total) : 0
        );
    }

    public List<Map<String, Object>> getZoneSummaries() {
        return slotRepository.findAll().stream()
                .collect(Collectors.groupingBy(s -> s.getZone() != null ? s.getZone() : "Unassigned"))
                .entrySet().stream()
                .map(e -> Map.<String, Object>of(
                        "zone", e.getKey(),
                        "total", e.getValue().size(),
                        "available", e.getValue().stream().filter(s -> s.getStatus() == SlotStatus.AVAILABLE).count()
                ))
                .toList();
    }

    public List<ParkingRecord> getSlotUsageHistory(Long slotId) {
        return parkingRecordRepository.findByParkingSlot_Id(slotId);
    }

    @Transactional
    public void createSlot(SlotRequest request) {
        if (slotRepository.existsBySlotNumber(request.getSlotNumber())) {
            throw new RuntimeException("Slot number already exists: " + request.getSlotNumber());
        }
        ParkingSlot slot = ParkingSlot.builder()
                .slotNumber(request.getSlotNumber())
                .zone(request.getZone())
                .floor(request.getFloor())
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(request.getHourlyRate())
                .description(request.getDescription())
                .build();
        slotRepository.save(slot);

        auditLogService.logAction(
                "SLOT_CREATED",
                "ADMIN",
                "ParkingSlot",
                slot.getId(),
                "Parking slot created: " + slot.getSlotNumber(),
                null,
                null,
                null
        );
    }

    @Transactional
    public void updateSlot(Long id, SlotRequest request) {
        ParkingSlot slot = getSlotById(id);
        slot.setSlotNumber(request.getSlotNumber());
        slot.setZone(request.getZone());
        slot.setFloor(request.getFloor());
        slot.setHourlyRate(request.getHourlyRate());
        slot.setDescription(request.getDescription());
        slotRepository.save(slot);
    }

    @Transactional
    public void deleteSlot(Long id) {
        ParkingSlot slot = getSlotById(id);
        String slotNumber = slot.getSlotNumber();
        slotRepository.deleteById(id);

        auditLogService.logAction(
                "SLOT_DELETED",
                "ADMIN",
                "ParkingSlot",
                id,
                "Parking slot deleted: " + slotNumber,
                null,
                null,
                null
        );
    }

    @Transactional
    public int batchGenerateSlots(BatchSlotRequest request) {
        List<ParkingSlot> slots = new ArrayList<>();
        for (int i = request.getStartFrom(); i < request.getStartFrom() + request.getCount(); i++) {
            String slotNumber = request.getPrefix() + "-" + String.format("%03d", i);
            if (slotRepository.existsBySlotNumber(slotNumber)) continue;
            slots.add(ParkingSlot.builder()
                    .slotNumber(slotNumber)
                    .zone(request.getZone())
                    .status(SlotStatus.AVAILABLE)
                    .hourlyRate(request.getHourlyRate())
                    .build());
        }
        slotRepository.saveAll(slots);
        return slots.size();
    }
}