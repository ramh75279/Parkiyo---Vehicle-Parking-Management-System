package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    // === Dashboard ===
    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.exitTime IS NULL")
    long countActiveRecords();

    @Query("SELECT COALESCE(SUM(0), 0)")
    BigDecimal calculateTodayRevenue();

    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.entryTime DESC LIMIT :limit")
    List<ParkingRecord> findTopRecentEntries(@Param("limit") int limit);

    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.createdAt DESC LIMIT 20")
    List<ParkingRecord> findTop20ByOrderByCreatedAtDesc();

    // ✅ NEW METHOD ADDED
    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.entryTime DESC LIMIT 20")
    List<ParkingRecord> findTop20ByOrderByEntryTimeDesc();

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.payment IS NOT NULL " +
            "ORDER BY pr.exitTime DESC LIMIT :limit")
    List<ParkingRecord> findRecentPaid(@Param("limit") int limit);

    // History & User related
    List<ParkingRecord> findByVehicleId(Long vehicleId);
    List<ParkingRecord> findBySlotId(Long slotId);
    List<ParkingRecord> findByUserEmail(String email);
    List<ParkingRecord> findByUserEmailAndActiveTrue(String email);
    List<ParkingRecord> findByActiveTrue();

    Optional<ParkingRecord> findByIdAndUserEmail(Long id, String email);

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.user.email = :email " +
            "ORDER BY pr.entryTime DESC LIMIT :limit")
    List<ParkingRecord> findByUserEmailRecent(@Param("email") String email, @Param("limit") int limit);

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.user.email = :email AND pr.exitTime IS NULL")
    Optional<ParkingRecord> findActiveByUserEmail(@Param("email") String email);

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.user.email = :email " +
            "AND pr.payment IS NULL ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findPendingPaymentsByUser(@Param("email") String email);

    List<ParkingRecord> findTop10ByOrderByEntryTimeDesc();

    // ==================== KEY METHODS FOR ENTRY & EXIT ====================

    @Query("SELECT pr FROM ParkingRecord pr " +
            "WHERE pr.vehicle.licensePlate = :licensePlate AND pr.active = true")
    Optional<ParkingRecord> findByVehicleLicensePlateAndActiveTrue(@Param("licensePlate") String licensePlate);

    @Query("SELECT COUNT(pr) > 0 FROM ParkingRecord pr " +
            "WHERE pr.vehicle.licensePlate = :licensePlate AND pr.active = true")
    boolean existsByVehicleLicensePlateAndActiveTrue(@Param("licensePlate") String licensePlate);
}