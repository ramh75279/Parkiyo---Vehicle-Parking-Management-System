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

import java.time.LocalDate;
import java.util.ArrayList;
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

    // Overloaded — used by ReservationController
    public List<ParkingSlot> getAvailableSlots(LocalDate date, String zone) {
        List<ParkingSlot> slots = slotRepository.findByStatus(SlotStatus.AVAILABLE);
        if (zone != null && !zone.isBlank()) {
            slots = slots.stream().filter(s -> zone.equalsIgnoreCase(s.getZone())).toList();
        }
        return slots;
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
        return parkingRecordRepository.findBySlotId(slotId);
    }

    @Transactional
    public void createSlot(SlotRequest request) {
        if (slotRepository.existsBySlotNumber(request.getSlotNumber())) {
            throw new RuntimeException("Slot number already exists: " + request.getSlotNumber());
        }
        ParkingSlot slot = ParkingSlot.builder()
                .slotNumber(request.getSlotNumber())
                .zone(request.getZone())
                .status(SlotStatus.AVAILABLE)
                .hourlyRate(request.getHourlyRate())
                .build();
        slotRepository.save(slot);

        auditLogService.logAction(
            "SLOT_CREATED",
            "ParkingSlot",
            slot.getId(),
            "Parking slot created: " + slot.getSlotNumber()
        );
    }

    @Transactional
    public void updateSlot(Long id, SlotRequest request) {
        ParkingSlot slot = getSlotById(id);
        slot.setSlotNumber(request.getSlotNumber());
        slot.setZone(request.getZone());
        slot.setHourlyRate(request.getHourlyRate());
        slotRepository.save(slot);
    }

    @Transactional
    public void deleteSlot(Long id) {
        ParkingSlot slot = getSlotById(id);
        slotRepository.deleteById(id);

        auditLogService.logAction(
                "SLOT_DELETED",
                "ParkingSlot",
                id,
                "Parking slot deleted: " + slot.getSlotNumber()
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
