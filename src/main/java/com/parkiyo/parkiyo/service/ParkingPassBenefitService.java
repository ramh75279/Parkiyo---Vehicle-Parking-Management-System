package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.ParkingSlot;
import com.parkiyo.parkiyo.model.PassPlan;
import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.model.UserPass;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Applies {@link PassPlan} rules at exit time: unlimited parking (fee 0) or percentage discount.
 * Zone on the plan must match the slot's zone, or plan zone must be ALL/blank.
 */
@Service
@RequiredArgsConstructor
public class ParkingPassBenefitService {

    private final UserPassService userPassService;

    public record ExitFeeResult(BigDecimal finalFee, UserPass appliedPass, String summary) {}

    public ExitFeeResult applyPassBenefits(User vehicleOwner,
                                           ParkingSlot slot,
                                           LocalDate exitDay,
                                           BigDecimal baseFee) {
        if (vehicleOwner == null || baseFee == null || baseFee.compareTo(BigDecimal.ZERO) <= 0) {
            return new ExitFeeResult(zeroOr(baseFee), null, "Standard rate");
        }

        Optional<UserPass> best = userPassService.findBestApplicablePass(vehicleOwner.getId(), slot, exitDay, baseFee);
        if (best.isEmpty()) {
            return new ExitFeeResult(baseFee.stripTrailingZeros(), null, "Standard rate");
        }

        UserPass up = best.get();
        PassPlan plan = up.getPassPlan();

        if (plan.isUnlimitedParking()) {
            return new ExitFeeResult(BigDecimal.ZERO, up, "Covered by pass: " + plan.getName() + " (unlimited)");
        }

        BigDecimal pct = plan.getDiscountPercent();
        if (pct == null || pct.compareTo(BigDecimal.ZERO) <= 0) {
            return new ExitFeeResult(baseFee.stripTrailingZeros(), null, "Standard rate");
        }

        BigDecimal factor = BigDecimal.ONE.subtract(
                pct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
        BigDecimal discounted = baseFee.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        if (discounted.compareTo(BigDecimal.ZERO) < 0) {
            discounted = BigDecimal.ZERO;
        }

        return new ExitFeeResult(discounted, up,
                "Pass discount " + pct.stripTrailingZeros().toPlainString() + "% (" + plan.getName() + ")");
    }

    private static BigDecimal zeroOr(BigDecimal baseFee) {
        if (baseFee == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return baseFee.setScale(2, RoundingMode.HALF_UP);
    }

    /** Pick best benefit among matching passes (lowest fee outcome). */
    public static Optional<UserPass> pickBestBenefit(List<UserPass> candidates, ParkingSlot slot, BigDecimal baseFee) {
        if (candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }
        return candidates.stream()
                .filter(up -> zoneMatches(up.getPassPlan(), slot))
                .min(Comparator.comparing((UserPass up) -> resultingFee(up.getPassPlan(), baseFee)));
    }

    private static BigDecimal resultingFee(PassPlan plan, BigDecimal baseFee) {
        if (plan.isUnlimitedParking()) {
            return BigDecimal.ZERO;
        }
        BigDecimal pct = plan.getDiscountPercent();
        if (pct == null || pct.compareTo(BigDecimal.ZERO) <= 0) {
            return baseFee;
        }
        BigDecimal factor = BigDecimal.ONE.subtract(pct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
        return baseFee.multiply(factor);
    }

    public static boolean zoneMatches(PassPlan plan, ParkingSlot slot) {
        if (plan == null) {
            return false;
        }
        String z = plan.getZone();
        if (z == null || z.isBlank() || "ALL".equalsIgnoreCase(z.trim())) {
            return true;
        }
        String slotZone = slot != null ? slot.getZone() : null;
        if (slotZone == null || slotZone.isBlank()) {
            return false;
        }
        return slotZone.trim().equalsIgnoreCase(z.trim());
    }
}
