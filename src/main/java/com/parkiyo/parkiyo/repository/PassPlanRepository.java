package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.PassPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassPlanRepository extends JpaRepository<PassPlan, Long> {

    List<PassPlan> findByActiveTrueOrderByDisplayOrderAscNameAsc();

    List<PassPlan> findAllByOrderByDisplayOrderAscNameAsc();
}
