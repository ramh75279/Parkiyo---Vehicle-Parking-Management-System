package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRecordRepository parkingRecordRepository;

    public List<ParkingRecord> getActiveRecordsByUser(String email) {
        return parkingRecordRepository.findByUserEmailAndActiveTrue(email);
    }

    public List<ParkingRecord> getAllActiveRecords() {
        return parkingRecordRepository.findByActiveTrue();
    }

    public List<ParkingRecord> getAllPastRecords() {
        return parkingRecordRepository.findAll().stream()
                .filter(record -> !record.isActive())
                .toList();
    }

    public List<ParkingRecord> getPastRecordsByUser(String email) {
        return parkingRecordRepository.findByUserEmail(email).stream()
                .filter(r -> !r.isActive())
                .toList();
    }

    public ParkingRecord getRecordByIdAndUser(Long id, String email) {
        return parkingRecordRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new RuntimeException("Parking record not found."));
    }

    public ParkingRecord getRecordById(Long id) {
        return parkingRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking record not found."));
    }

    public Map<String, Object> getTicket(Long recordId, String email) {
        ParkingRecord record = getRecordByIdAndUser(recordId, email);
        return Map.of(
                "record", record,
                "vehicle", record.getVehicle() != null ? record.getVehicle() : "N/A",
                "slot", record.getSlot() != null ? record.getSlot() : "N/A"
        );
    }

    public Map<String, Object> getAdminTicket(Long recordId) {
        ParkingRecord record = getRecordById(recordId);
        return Map.of(
                "record", record,
                "vehicle", record.getVehicle() != null ? record.getVehicle() : "N/A",
                "slot", record.getSlot() != null ? record.getSlot() : "N/A"
        );
    }
}