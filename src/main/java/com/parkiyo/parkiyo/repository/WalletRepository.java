package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserEmail(String email);

    @Query("SELECT w FROM Wallet w LEFT JOIN FETCH w.transactions WHERE w.user.email = :email")
    Optional<Wallet> findByUserEmailWithTransactions(@Param("email") String email);

    Optional<Wallet> findByUserId(Long userId);
}
