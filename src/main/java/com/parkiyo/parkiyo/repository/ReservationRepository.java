package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserEmail(String email);

    List<Reservation> findByUserEmailAndStatus(String email, ReservationStatus status);

    Optional<Reservation> findByReservationCode(String reservationCode);

    Optional<Reservation> findTopByUserEmailAndStatusOrderByCreatedAtDesc(
            String email, ReservationStatus status);

    List<Reservation> findBySlotId(Long slotId);

    boolean existsByReservationCode(String reservationCode);

    // ====================== COUNT METHODS FOR STATS ======================

    /**
     * Count reservations by user and status
     */
    long countByUserEmailAndStatus(String email, ReservationStatus status);

    /**
     * Count CONFIRMED reservations for today
     */
    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user.email = :email " +
            "AND r.status = :status " +
            "AND FUNCTION('DATE', r.startTime) = :today")
    long countByUserEmailAndStatusAndStartTimeToday(
            @Param("email") String email,
            @Param("status") ReservationStatus status,
            @Param("today") LocalDate today);

    /**
     * Optional: Count all reservations for a user (for future use)
     */
    long countByUserEmail(String email);
}