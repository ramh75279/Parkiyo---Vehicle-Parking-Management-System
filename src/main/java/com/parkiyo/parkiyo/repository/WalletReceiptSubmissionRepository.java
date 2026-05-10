package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.WalletReceiptStatus;
import com.parkiyo.parkiyo.model.WalletReceiptSubmission;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WalletReceiptSubmissionRepository extends JpaRepository<WalletReceiptSubmission, Long> {

    boolean existsByUser_IdAndStatus(Long userId, WalletReceiptStatus status);

    Optional<WalletReceiptSubmission> findFirstByUser_EmailAndStatusOrderByCreatedAtDesc(
            String email, WalletReceiptStatus status);

    @EntityGraph(attributePaths = {"user"})
    List<WalletReceiptSubmission> findByStatusOrderByCreatedAtAsc(WalletReceiptStatus status);

    @EntityGraph(attributePaths = {"user", "reviewedBy"})
    List<WalletReceiptSubmission> findByStatusInOrderByCreatedAtDesc(
            Collection<WalletReceiptStatus> statuses, Pageable pageable);
}
