package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionCode(String transactionCode);

    List<Payment> findByUserEmail(String email);

    long countByUserEmail(String email);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByUserEmailAndStatus(String email, PaymentStatus status);

    List<Payment> findByParkingRecordId(Long parkingRecordId);

    /**
     * Most recent successful payment for a user
     */
    Optional<Payment> findTopByUserEmailAndStatusOrderByPaidAtDesc(
            String email, PaymentStatus status);

    /**
     * Total revenue from all successful payments
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = :status")
    Double sumTotalRevenue(@Param("status") PaymentStatus status);

    /**
     * Revenue by specific user
     */
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
            "WHERE p.user.email = :email AND p.status = :status")
    Double sumRevenueByUser(@Param("email") String email,
                            @Param("status") PaymentStatus status);

    /**
     * Latest 10 payments (for dashboard/admin)
     */
    List<Payment> findTop10ByOrderByCreatedAtDesc();

    // Optional: You can add this for convenience
    default Double sumTotalRevenue() {
        return sumTotalRevenue(PaymentStatus.SUCCESS);
    }
}