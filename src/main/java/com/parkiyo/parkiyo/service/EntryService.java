package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.enums.NotificationType;
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
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final ParkingRecordRepository parkingRecordRepository;
    private final ParkingSlotRepository slotRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    private static final ZoneId COLOMBO_ZONE = ZoneId.of("Asia/Colombo");

    @Transactional
    public void processEntry(EntryRequest request, String operatorEmail) {
        String normalizedPlate = normalizeLicensePlate(request.getLicensePlate());

        // 1. Check for duplicate active entry
        Optional<ParkingRecord> existing = parkingRecordRepository.findByVehicleLicensePlateAndActiveTrue(normalizedPlate);
        if (existing.isPresent()) {
            throw new RuntimeException("Vehicle " + normalizedPlate + " is already parked.");
        }

        // 2. Get or Create Vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(normalizedPlate)
                .orElseGet(() -> {
                    VehicleCategory category = convertToVehicleCategory(request.getVehicleType());
                    Vehicle v = Vehicle.builder()
                            .licensePlate(normalizedPlate)
                            .category(category)
                            .active(true)
                            .build();
                    return vehicleRepository.save(v);
                });

        // 3. Auto-assign available slot
        ParkingSlot slot = slotRepository
                .findFirstByStatusAndVehicleCategoryOrderBySlotNumberAsc(SlotStatus.AVAILABLE, vehicle.getCategory())
                .or(() -> slotRepository.findFirstByStatusOrderBySlotNumberAsc(SlotStatus.AVAILABLE))
                .orElseThrow(() -> new RuntimeException("No available slot for vehicle type: " + request.getVehicleType()));

        // 4. Get User (operator or owner)
        User user = userRepository.findByEmail(operatorEmail).orElse(null);

        // 5. Create Parking Record
        LocalDateTime entryTime = LocalDateTime.now(COLOMBO_ZONE);

        ParkingRecord record = ParkingRecord.builder()
                .vehicle(vehicle)
                .slot(slot)
                .user(user)
                .entryTime(entryTime)
                .entryOperator(operatorEmail)
                .active(true)
                .build();

        parkingRecordRepository.save(record);

        // 6. Mark slot occupied
        slot.setStatus(SlotStatus.OCCUPIED);
        slotRepository.save(slot);

        // Notifications & Audit
        notificationService.createNotification(user, NotificationType.ENTRY,
                "Vehicle Entry",
                "Vehicle " + normalizedPlate + " entered slot " + slot.getSlotNumber(), "/parking");

        auditLogService.logAction(
                "ENTRY",
                operatorEmail,
                "ParkingRecord",
                record.getId(),
                "Vehicle " + normalizedPlate + " entered slot " + slot.getSlotNumber(),
                null,
                null,
                null
        );
    }

    private String normalizeLicensePlate(String plate) {
        if (plate == null) return "";
        return plate.toUpperCase().replaceAll("\\s+", "").replace("-", "");
    }

    private VehicleCategory convertToVehicleCategory(String type) {
        try {
            return VehicleCategory.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            return VehicleCategory.CAR;
        }
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTop20ByOrderByEntryTimeDesc()
                .stream().limit(limit).toList();
    }
}