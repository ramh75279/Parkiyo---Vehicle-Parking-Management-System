package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.EntryRequest;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void processEntry(EntryRequest request, String operatorEmail) {
        VehicleCategory category = (request.getCategory() != null && !request.getCategory().isBlank())
            ? VehicleCategory.valueOf(request.getCategory().trim().toUpperCase())
            : VehicleCategory.CAR;

        // Get or create vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(
                        request.getLicensePlate().toUpperCase())
                .orElseGet(() -> {
                    Vehicle v = Vehicle.builder()
                            .licensePlate(request.getLicensePlate().toUpperCase())
                    .category(category)
                            .active(true)
                            .build();
                    return vehicleRepository.save(v);
                });

        // Get slot and check availability
        ParkingSlot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found."));
        if (slot.getStatus() != SlotStatus.AVAILABLE) {
            throw new RuntimeException("Slot " + slot.getSlotNumber() + " is not available.");
        }

        // Get user (may be null for walk-in vehicles)
        User user = userRepository.findByEmail(operatorEmail).orElse(null);

        // Create parking record
        ParkingRecord record = ParkingRecord.builder()
                .vehicle(vehicle)
                .slot(slot)
                .user(user)
                .entryTime(LocalDateTime.now())
                .entryOperator(operatorEmail)
                .active(true)
                .build();
        parkingRecordRepository.save(record);

        // Mark slot as occupied
        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTop20ByOrderByCreatedAtDesc()
                .stream().limit(limit).toList();
    }
}
