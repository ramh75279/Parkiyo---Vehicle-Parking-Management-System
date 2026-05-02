package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import com.parkiyo.parkiyo.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    /** Prefer slots tagged for this vehicle category; see EntryService for fallback when none match. */
    Optional<ParkingSlot> findFirstByStatusAndVehicleCategoryOrderBySlotNumberAsc(SlotStatus status, VehicleCategory category);

    /** Simple fallback - any available slot */
    Optional<ParkingSlot> findFirstByStatusOrderBySlotNumberAsc(SlotStatus status);

    /** Count available slots by vehicle category */
    long countByStatusAndVehicleCategory(SlotStatus status, VehicleCategory category);
}