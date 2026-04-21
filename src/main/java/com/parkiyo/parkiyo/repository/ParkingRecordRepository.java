package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    // Active records
    List<ParkingRecord> findByActiveTrue();

    List<ParkingRecord> findByUserEmailAndActiveTrue(String email);

    List<ParkingRecord> findByUserEmail(String email);

    Optional<ParkingRecord> findByIdAndUserEmail(Long id, String email);

    List<ParkingRecord> findByVehicleId(Long vehicleId);

    List<ParkingRecord> findBySlotId(Long slotId);

    // Recent entries (for admin dashboard)
    List<ParkingRecord> findTop20ByOrderByCreatedAtDesc();

    // Revenue queries
    @Query("SELECT SUM(p.amountCharged) FROM ParkingRecord p WHERE p.exitTime BETWEEN :from AND :to")
    Double sumRevenueByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT COUNT(p) FROM ParkingRecord p WHERE p.entryTime BETWEEN :from AND :to")
    long countEntriesByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
