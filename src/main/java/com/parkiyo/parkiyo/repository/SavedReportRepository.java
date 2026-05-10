package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.SavedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedReportRepository extends JpaRepository<SavedReport, Long>, JpaSpecificationExecutor<SavedReport> {
}
