package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    void deleteByWallet_Id(Long walletId);

    void deleteByPayment_Id(Long paymentId);
}
