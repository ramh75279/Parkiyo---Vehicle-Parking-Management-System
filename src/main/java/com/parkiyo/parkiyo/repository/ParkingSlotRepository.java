package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.SlotStatus;
import com.parkiyo.parkiyo.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    long countByStatus(SlotStatus status);

    List<ParkingSlot> findByStatus(SlotStatus status);

    boolean existsBySlotNumber(String slotNumber);

    List<ParkingSlot> findByZone(String zone);

    @Query("SELECT COUNT(s) FROM ParkingSlot s WHERE s.status = 'OCCUPIED'")
    long countByStatusOccupied();

    List<ParkingSlot> findTop10ByOrderBySlotNumberAsc();
}