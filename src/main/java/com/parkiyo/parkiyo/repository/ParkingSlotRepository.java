package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    Optional<ParkingSlot> findBySlotNumber(String slotNumber);

    boolean existsBySlotNumber(String slotNumber);

    List<ParkingSlot> findByStatus(SlotStatus status);

    List<ParkingSlot> findByZone(String zone);

    List<ParkingSlot> findByZoneAndStatus(String zone, SlotStatus status);

    List<String> findDistinctZoneBy();

    long countByStatus(SlotStatus status);
}
