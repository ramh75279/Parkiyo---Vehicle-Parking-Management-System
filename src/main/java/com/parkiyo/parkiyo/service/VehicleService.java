package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.dto.VehicleRequest;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.repository.ParkingRecordRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import com.parkiyo.parkiyo.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ParkingRecordRepository parkingRecordRepository;

    public List<Vehicle> getAllVehicles(String search, String category) {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        if (search != null && !search.isBlank()) {
            String q = search.toLowerCase();
            vehicles = vehicles.stream()
                    .filter(v -> v.getLicensePlate().toLowerCase().contains(q)
                            || (v.getMake() != null && v.getMake().toLowerCase().contains(q))
                            || (v.getModel() != null && v.getModel().toLowerCase().contains(q)))
                    .toList();
        }
        if (category != null && !category.isBlank()) {
            VehicleCategory cat = VehicleCategory.valueOf(category.toUpperCase());
            vehicles = vehicles.stream().filter(v -> v.getCategory() == cat).toList();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    public List<Vehicle> getVehiclesByUser(String email) {
        return vehicleRepository.findByUserEmail(email);
    }

    public List<Vehicle> getVehiclesByCategory(String category) {
        if (category == null || category.isBlank()) return vehicleRepository.findAll();
        return vehicleRepository.findByCategory(VehicleCategory.valueOf(category.toUpperCase()));
    }

    public List<String> getAllCategories() {
        return Arrays.stream(VehicleCategory.values()).map(Enum::name).toList();
    }

    public List<ParkingRecord> getVehicleParkingHistory(Long vehicleId) {
        return parkingRecordRepository.findByVehicleId(vehicleId);
    }

    @Transactional
    public void createVehicle(VehicleRequest request) {
        if (vehicleRepository.existsByLicensePlate(request.getLicensePlate())) {
            throw new RuntimeException("License plate already registered: " + request.getLicensePlate());
        }
        User user = request.getUserId() != null
                ? userRepository.findById(request.getUserId()).orElse(null)
                : null;

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.getLicensePlate().toUpperCase())
                .category(request.getCategory())
                .make(request.getMake())
                .model(request.getModel())
                .color(request.getColor())
                .year(request.getYear())
                .user(user)
                .active(true)
                .build();
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void updateVehicle(Long id, VehicleRequest request) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setLicensePlate(request.getLicensePlate().toUpperCase());
        vehicle.setCategory(request.getCategory());
        vehicle.setMake(request.getMake());
        vehicle.setModel(request.getModel());
        vehicle.setColor(request.getColor());
        vehicle.setYear(request.getYear());
        vehicleRepository.save(vehicle);
    }

    @Transactional
    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    @Transactional
    public void quickRegisterByPlate(String licensePlate, String category) {
        if (vehicleRepository.existsByLicensePlate(licensePlate)) {
            throw new RuntimeException("License plate already registered: " + licensePlate);
        }
        VehicleCategory cat = (category != null && !category.isBlank())
                ? VehicleCategory.valueOf(category.toUpperCase())
                : VehicleCategory.CAR;

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(licensePlate.toUpperCase())
                .category(cat)
                .active(true)
                .build();
        vehicleRepository.save(vehicle);
    }

    // Placeholder for CSV/Excel import — extend as needed
    public void uploadImportFile(MultipartFile file) {
        if (file.isEmpty()) throw new RuntimeException("Uploaded file is empty.");
        // TODO: parse CSV/Excel rows and stage them for confirmation
    }

    public int confirmImport() {
        // TODO: save staged vehicles to DB
        return 0;
    }

    public void clearPendingImport() {
        // TODO: clear staged import data
    }
}
