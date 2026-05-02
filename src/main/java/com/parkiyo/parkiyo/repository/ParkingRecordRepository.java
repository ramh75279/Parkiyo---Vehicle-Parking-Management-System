package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.ParkingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("SELECT COALESCE(SUM(pr.amount), 0) FROM ParkingRecord pr " +
            "WHERE pr.exitTime IS NOT NULL AND DATE(pr.exitTime) = CURRENT_DATE")
    BigDecimal calculateTodayRevenue();

    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findTopRecentEntries(@Param("limit") int limit);  // Use Pageable in service for better LIMIT

    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.createdAt DESC")
    List<ParkingRecord> findTop20ByOrderByCreatedAtDesc();

    @Query("SELECT pr FROM ParkingRecord pr ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findTop20ByOrderByEntryTimeDesc();

    @Query("SELECT pr FROM ParkingRecord pr WHERE pr.payment IS NOT NULL " +
            "ORDER BY pr.exitTime DESC")
    List<ParkingRecord> findRecentPaid(@Param("limit") int limit);

    // History & User related
    List<ParkingRecord> findByVehicleId(Long vehicleId);

    @Query("SELECT DISTINCT pr FROM ParkingRecord pr " +
            "LEFT JOIN FETCH pr.parkingSlot " +
            "WHERE pr.vehicle.id = :vehicleId " +
            "ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findParkingHistoryForVehicle(@Param("vehicleId") Long vehicleId);

    List<ParkingRecord> findByParkingSlot_Id(Long slotId);
    List<ParkingRecord> findByUserEmail(String email);
    List<ParkingRecord> findByUserEmailAndActiveTrue(String email);
    List<ParkingRecord> findByActiveTrue();

    Optional<ParkingRecord> findByIdAndUserEmail(Long id, String email);

    @Query(value = "SELECT * FROM parking_records pr JOIN users u ON pr.user_id = u.id " +
            "WHERE u.email = :email ORDER BY pr.entry_time DESC LIMIT :limit", nativeQuery = true)
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

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ParkingRecord pr WHERE (pr.user IS NOT NULL AND pr.user.id = :userId) " +
            "OR (pr.vehicle.user IS NOT NULL AND pr.vehicle.user.id = :userId)")
    void deleteAllLinkedToUser(@Param("userId") Long userId);
}