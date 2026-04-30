package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findAllByOrderByCreatedAtDesc();

    @Query("SELECT DISTINCT a.action FROM AuditLog a WHERE a.action IS NOT NULL ORDER BY a.action")
    List<String> findDistinctActions();

    @Modifying
    @Query("UPDATE AuditLog a SET a.performedBy = null WHERE a.performedBy.id = :userId")
    int clearPerformedBy(@Param("userId") Long userId);
}
