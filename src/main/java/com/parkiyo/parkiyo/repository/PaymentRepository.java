package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionCode(String transactionCode);

    List<Payment> findByUserEmail(String email);

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
}
