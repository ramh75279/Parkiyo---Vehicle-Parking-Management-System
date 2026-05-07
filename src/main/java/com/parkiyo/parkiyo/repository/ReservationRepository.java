package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.ReservationStatus;
import com.parkiyo.parkiyo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    long countByUserEmailAndStatus(String email, ReservationStatus status);

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.user.email = :email " +
            "AND r.status = :status " +
            "AND FUNCTION('DATE', r.startTime) = :today")
    long countByUserEmailAndStatusAndStartTimeToday(
            @Param("email") String email,
            @Param("status") ReservationStatus status,
            @Param("today") LocalDate today);

    long countByUserEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Reservation r WHERE r.user.id = :userId")
    void deleteByUser_Id(@Param("userId") Long userId);
}