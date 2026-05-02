package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser_Email(String email);

    Optional<Wallet> findByUser_Id(Long userId);
}