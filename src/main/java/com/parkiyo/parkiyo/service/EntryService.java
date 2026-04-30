package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.dto.EntryResponse;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.ParkingSlotRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSlotRepository slotRepository;
    private final UserRepository userRepository;

    @Transactional
    public EntryResponse processEntry(EntryRequest request, String operatorEmail) {
        String normalizedPlate = normalizePlate(request.getLicensePlate());

        Vehicle vehicle = vehicleRepository.findByLicensePlate(normalizedPlate)
                .orElseGet(() -> vehicleRepository.save(
                        Vehicle.builder()
                                .licensePlate(normalizedPlate)
                                .category(resolveCategory(request.getVehicleType()))
                                .active(true)
                                .build()
                ));

        if (vehicle.getCategory() == null) {
            vehicle.setCategory(resolveCategory(request.getVehicleType()));
            vehicleRepository.save(vehicle);
        }

        ParkingSlot slot = resolveSlot(request.getPreferredSlotId());
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Selected slot is not available.");
        }

        User user = userRepository.findByEmail(operatorEmail).orElse(null);

        ParkingRecord record = ParkingRecord.builder()
                .vehicle(vehicle)
                .slot(slot)
                .user(user)
                .entryTime(LocalDateTime.now())
                .entryOperator(operatorEmail)
                .active(true)
                .build();
        parkingRecordRepository.save(record);

        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        EntryResponse response = new EntryResponse();
        response.setLicensePlate(vehicle.getLicensePlate());
        response.setVehicleType(vehicle.getCategory().name());
        response.setMessage("Vehicle entry recorded successfully");
        response.setStatus("SUCCESS");
        return response;
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findAll().stream()
                .sorted(Comparator.comparing(
                        ParkingRecord::getEntryTime,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .limit(limit)
                .toList();
    }

    // Backward-compatible signature kept for older callers.
    public EntryResponse processEntry(EntryRequest request) {
        return processEntry(request, "system");
    }

    private ParkingSlot resolveSlot(Long preferredSlotId) {
        if (preferredSlotId != null) {
            return slotRepository.findById(preferredSlotId)
                    .orElseThrow(() -> new RuntimeException("Selected slot not found."));
        }
        return slotRepository.findByStatus(SlotStatus.AVAILABLE).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available parking slots."));
    }

    private VehicleCategory resolveCategory(String vehicleType) {
        if (vehicleType == null || vehicleType.isBlank()) {
            return VehicleCategory.CAR;
        }
        String normalized = vehicleType.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "BIKE" -> VehicleCategory.MOTORCYCLE;
            case "THREE_WHEELER" -> VehicleCategory.CAR;
            default -> VehicleCategory.valueOf(normalized);
        };
    }

    private String normalizePlate(String plate) {
        if (plate == null || plate.isBlank()) {
            throw new RuntimeException("License plate is required.");
        }
        return plate.trim().toUpperCase(Locale.ROOT);
    }
}