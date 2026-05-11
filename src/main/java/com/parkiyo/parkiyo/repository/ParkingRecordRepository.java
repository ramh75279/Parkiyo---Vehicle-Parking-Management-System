package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.ParkingRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRecordRepository extends JpaRepository<ParkingRecord, Long> {

    // === Dashboard ===
    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.exitTime IS NULL")
    long countActiveRecords();

    /** Native SQL: Hibernate 6 rejects {@code DATE(localDateTime) = CURRENT_DATE} in HQL (type mismatch). */
    @Query(value = "SELECT COALESCE(SUM(amount), 0) FROM parking_records "
            + "WHERE exit_time IS NOT NULL AND CAST(exit_time AS DATE) = CURRENT_DATE",
            nativeQuery = true)
    BigDecimal calculateTodayRevenue();

    /** Loads associations for admin dashboard / Live Ops (works with open-in-view disabled). */
    @Query("SELECT DISTINCT pr FROM ParkingRecord pr "
            + "JOIN FETCH pr.vehicle v "
            + "LEFT JOIN FETCH pr.parkingSlot "
            + "LEFT JOIN FETCH pr.user "
            + "LEFT JOIN FETCH v.user "
            + "LEFT JOIN FETCH pr.payment "
            + "ORDER BY pr.entryTime DESC")
    List<ParkingRecord> findRecentEntriesForDashboard(Pageable pageable);

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

    /**
     * Active sessions the user may exit: entered under their account or owning the vehicle.
     * Fetches vehicle and slot for rendering with open-in-view disabled.
     */
    @Query("SELECT DISTINCT pr FROM ParkingRecord pr " +
            "JOIN FETCH pr.vehicle v " +
            "LEFT JOIN FETCH pr.parkingSlot " +
            "WHERE pr.active = true AND (pr.user.email = :email OR (v.user IS NOT NULL AND v.user.email = :email))")
    List<ParkingRecord> findActiveRecordsVisibleToUser(@Param("email") String email);

    List<ParkingRecord> findByActiveTrue();

    Optional<ParkingRecord> findByIdAndUserEmail(Long id, String email);

    @Query("SELECT pr FROM ParkingRecord pr " +
            "JOIN FETCH pr.vehicle v " +
            "LEFT JOIN FETCH v.user " +
            "LEFT JOIN FETCH pr.user " +
            "WHERE pr.id = :id AND pr.active = true " +
            "AND (pr.user.email = :email OR (v.user IS NOT NULL AND v.user.email = :email))")
    Optional<ParkingRecord> findActiveByIdVisibleToAccount(@Param("id") Long id, @Param("email") String email);

    @Query(value = "SELECT * FROM parking_records WHERE user_id = " +
            "(SELECT id FROM users WHERE email = :email) " +
            "ORDER BY entry_time DESC LIMIT :limit", nativeQuery = true)
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

    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.user.email = :email " +
            "AND pr.entryTime >= :start AND pr.entryTime < :end")
    long countUserEntriesBetween(@Param("email") String email,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(pr) FROM ParkingRecord pr WHERE pr.user.email = :email " +
            "AND pr.exitTime IS NOT NULL AND pr.exitTime >= :start AND pr.exitTime < :end")
    long countUserExitsBetween(@Param("email") String email,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT pr.id) FROM ParkingRecord pr WHERE pr.user.email = :email " +
            "AND ((pr.entryTime >= :start AND pr.entryTime < :end) " +
            "OR (pr.exitTime IS NOT NULL AND pr.exitTime >= :start AND pr.exitTime < :end))")
    long countUserParkingTouchBetween(@Param("email") String email,
                                      @Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}