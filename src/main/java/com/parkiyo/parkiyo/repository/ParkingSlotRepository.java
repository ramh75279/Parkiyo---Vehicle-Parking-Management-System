package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    long countByStatus(SlotStatus status);

    List<ParkingSlot> findByStatus(SlotStatus status);

    boolean existsBySlotNumber(String slotNumber);

    List<ParkingSlot> findByZone(String zone);

    @Query("SELECT COUNT(s) FROM ParkingSlot s WHERE s.status = 'OCCUPIED'")
    long countByStatusOccupied();

    List<ParkingSlot> findTop10ByOrderBySlotNumberAsc();

    // ==================== IMPORTANT FOR ENTRY ====================

    /** Find first available slot for a specific vehicle category */
    @Query("SELECT s FROM ParkingSlot s WHERE s.status = 'AVAILABLE' " +
            "AND s.vehicleCategory = :category " +
            "ORDER BY s.slotNumber ASC LIMIT 1")
    Optional<ParkingSlot> findFirstAvailableSlotByCategory(@Param("category") VehicleCategory category);

    /** Simple fallback - any available slot */
    Optional<ParkingSlot> findFirstByStatusOrderBySlotNumberAsc(SlotStatus status);

    /** Count available slots by vehicle category */
    long countByStatusAndVehicleCategory(SlotStatus status, VehicleCategory category);
}