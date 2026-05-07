package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.EntryRequest;
import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.exception.BadRequestException;
import com.parkiyo.parkiyo.exception.ResourceNotFoundException;
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
        if (request == null || request.getLicensePlate() == null || request.getLicensePlate().trim().isEmpty()) {
            throw new BadRequestException("License plate is required");
        }

        String normalizedPlate = normalizeLicensePlate(request.getLicensePlate());

        // 1. Prevent duplicate entry
        if (parkingRecordRepository.existsByVehicleLicensePlateAndActiveTrue(normalizedPlate)) {
            throw new BadRequestException("Vehicle " + normalizedPlate + " is already parked.");
        }

        // 2. Get or Create Vehicle
        Vehicle vehicle = vehicleRepository.findByLicensePlate(normalizedPlate)
                .orElseGet(() -> createNewVehicle(request, normalizedPlate));

        // 3. Find available slot
        ParkingSlot slot = findAvailableSlot(vehicle.getCategory())
                .orElseThrow(() -> new BadRequestException("No available parking slot at the moment."));

        // 4. Get Operator
        User operator = userRepository.findByEmail(operatorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Operator not found: " + operatorEmail));

        // 5. Create Parking Record
        LocalDateTime entryTime = LocalDateTime.now(COLOMBO_ZONE);

        ParkingRecord record = ParkingRecord.builder()
                .vehicle(vehicle)
                .slot(slot)
                .user(operator)
                .entryTime(entryTime)
                .entryOperator(operatorEmail)
                .active(true)
                .build();

        parkingRecordRepository.save(record);

        // 6. Update slot status
        slot.setStatus(SlotStatus.OCCUPIED);
        // slot.setLastOccupiedTime(entryTime);   // Removed - field doesn't exist yet
        slotRepository.save(slot);

        // 7. Audit & Notification
        auditLogService.logAction(
                "VEHICLE_ENTRY",
                operatorEmail,
                "ParkingRecord",
                record.getId(),
                "Vehicle " + normalizedPlate + " entered slot " + slot.getSlotNumber(),
                null,
                null,
                null
        );

        notificationService.createNotification(
                operator,
                NotificationType.ENTRY,
                "Vehicle Entry",
                "Vehicle " + normalizedPlate + " entered slot " + slot.getSlotNumber(),
                "/parking"
        );
    }

    private Vehicle createNewVehicle(EntryRequest request, String normalizedPlate) {
        VehicleCategory category = convertToVehicleCategory(request.getVehicleType());

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(normalizedPlate)
                .category(category)
                .active(true)
                .build();

        return vehicleRepository.save(vehicle);
    }

    private Optional<ParkingSlot> findAvailableSlot(VehicleCategory category) {
        return slotRepository
                .findFirstByStatusAndPreferredVehicleCategoryOrderBySlotNumberAsc(SlotStatus.AVAILABLE, category)
                .or(() -> slotRepository.findFirstByStatusOrderBySlotNumberAsc(SlotStatus.AVAILABLE));
    }

    private String normalizeLicensePlate(String plate) {
        if (plate == null) return "";
        return plate.trim()
                .toUpperCase()
                .replaceAll("\\s+", "")
                .replace("-", "")
                .replace(".", "");
    }

    private VehicleCategory convertToVehicleCategory(String type) {
        if (type == null || type.trim().isEmpty()) {
            return VehicleCategory.CAR;
        }
        try {
            return VehicleCategory.valueOf(type.trim().toUpperCase());
        } catch (Exception e) {
            return VehicleCategory.CAR;
        }
    }

    public List<ParkingRecord> getRecentEntries(int limit) {
        return parkingRecordRepository.findTop20ByOrderByEntryTimeDesc()
                .stream()
                .limit(limit)
                .toList();
    }
}