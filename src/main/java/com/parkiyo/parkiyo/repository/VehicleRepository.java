package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);

    List<Vehicle> findByUserId(Long userId);

    List<Vehicle> findByUserEmail(String email);

    List<Vehicle> findByCategory(VehicleCategory category);

    List<Vehicle> findByActiveTrue();

    List<Vehicle> findByLicensePlateContainingIgnoreCase(String licensePlate);
}
