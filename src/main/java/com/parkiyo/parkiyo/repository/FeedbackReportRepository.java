package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.FeedbackCategory;
import com.parkiyo.parkiyo.enums.FeedbackReportStatus;
import com.parkiyo.parkiyo.model.FeedbackReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackReportRepository extends JpaRepository<FeedbackReport, Long> {

    List<FeedbackReport> findByUser_EmailOrderByCreatedAtDesc(String email);

    Optional<FeedbackReport> findByIdAndUser_Email(Long id, String email);

    long countByStatus(FeedbackReportStatus status);

    @Query("SELECT COUNT(f) FROM FeedbackReport f WHERE f.category = :category")
    long countByCategory(@Param("category") FeedbackCategory category);

    @Query("SELECT COALESCE(AVG(f.rating), 0) FROM FeedbackReport f WHERE f.rating IS NOT NULL")
    double averageRatingSubmitted();

    @Query("SELECT f FROM FeedbackReport f JOIN FETCH f.user ORDER BY f.createdAt DESC")
    List<FeedbackReport> findAllForAdminOrderByCreatedAtDesc();

    @Query("SELECT f FROM FeedbackReport f JOIN FETCH f.user WHERE (:status IS NULL OR f.status = :status) "
            + "AND (:category IS NULL OR f.category = :category) ORDER BY f.createdAt DESC")
    List<FeedbackReport> findFiltered(
            @Param("status") FeedbackReportStatus status,
            @Param("category") FeedbackCategory category);

    @Query("SELECT f FROM FeedbackReport f JOIN FETCH f.user WHERE f.id = :id")
    Optional<FeedbackReport> findByIdWithUser(@Param("id") Long id);
}
