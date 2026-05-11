package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {

    Optional<Payment> findByTransactionCode(String transactionCode);

    List<Payment> findByUserEmail(String email);

    Page<Payment> findByUserEmail(String email, Pageable pageable);

    long countByUser_Email(String email);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByUserEmailAndStatus(String email, PaymentStatus status);

    List<Payment> findByParkingRecordId(Long parkingRecordId);

    // Most recent successful payment for a user
    Optional<Payment> findTopByUserEmailAndStatusOrderByPaidAtDesc(String email, PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'SUCCESS'")
    Double sumTotalRevenue();

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.user.email = :email AND p.status = 'SUCCESS'")
    Double sumRevenueByUser(String email);

    List<Payment> findTop10ByOrderByCreatedAtDesc();

    List<Payment> findByUser_Id(Long userId);

    /**
     * Loads payment with reservation graph so controllers/templates can read
     * reservation, slot, vehicle, and user after the service transaction ends.
     */
    @Query("""
            SELECT DISTINCT p FROM Payment p
            LEFT JOIN FETCH p.reservation r
            LEFT JOIN FETCH r.slot
            LEFT JOIN FETCH r.vehicle
            LEFT JOIN FETCH r.user
            WHERE p.id = :id
            """)
    Optional<Payment> findByIdWithReservationDetails(@Param("id") Long id);
}