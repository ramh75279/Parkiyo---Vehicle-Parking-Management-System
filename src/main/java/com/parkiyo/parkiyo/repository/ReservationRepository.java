package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
