package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.PassPlanCategory;
import com.parkiyo.parkiyo.model.PassPlan;
import com.parkiyo.parkiyo.repository.PassPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassPlanService {

    private final PassPlanRepository passPlanRepository;

    @Transactional(readOnly = true)
    public List<PassPlan> listAllForAdmin() {
        return passPlanRepository.findAllByOrderByDisplayOrderAscNameAsc();
    }

    @Transactional(readOnly = true)
    public List<PassPlan> listActiveCatalog() {
        return passPlanRepository.findByActiveTrueOrderByDisplayOrderAscNameAsc();
    }

    @Transactional(readOnly = true)
    public PassPlan getById(Long id) {
        return passPlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pass plan not found"));
    }

    @Transactional
    public PassPlan savePlan(Long id,
                             String name,
                             String description,
                             PassPlanCategory category,
                             BigDecimal price,
                             int durationDays,
                             String zone,
                             boolean unlimitedParking,
                             BigDecimal discountPercent,
                             boolean active,
                             int displayOrder) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valid price is required");
        }
        if (durationDays < 1) {
            throw new IllegalArgumentException("Duration must be at least 1 day");
        }
        BigDecimal disc = discountPercent != null ? discountPercent : BigDecimal.ZERO;
        if (disc.compareTo(BigDecimal.ZERO) < 0 || disc.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        if (unlimitedParking) {
            disc = BigDecimal.ZERO;
        }

        PassPlan plan;
        if (id != null) {
            plan = getById(id);
        } else {
            plan = new PassPlan();
        }

        plan.setName(name.trim());
        plan.setDescription(description != null ? description.trim() : null);
        plan.setCategory(category != null ? category : PassPlanCategory.MONTHLY);
        plan.setPrice(price);
        plan.setDurationDays(durationDays);
        plan.setZone(zone != null && !zone.isBlank() ? zone.trim() : null);
        plan.setUnlimitedParking(unlimitedParking);
        plan.setDiscountPercent(disc);
        plan.setActive(active);
        plan.setDisplayOrder(displayOrder);

        return passPlanRepository.save(plan);
    }

    @Transactional
    public void deactivate(Long id) {
        PassPlan plan = getById(id);
        plan.setActive(false);
        passPlanRepository.save(plan);
    }
}
