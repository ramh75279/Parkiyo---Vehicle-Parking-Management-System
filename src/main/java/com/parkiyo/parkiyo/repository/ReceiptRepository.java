package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Optional<Receipt> findByPaymentId(Long paymentId);
}
