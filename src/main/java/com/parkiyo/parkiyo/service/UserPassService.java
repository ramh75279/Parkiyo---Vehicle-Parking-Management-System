package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.enums.NotificationType;
import com.parkiyo.parkiyo.enums.UserPassStatus;
import com.parkiyo.parkiyo.model.*;
import com.parkiyo.parkiyo.repository.PassPlanRepository;
import com.parkiyo.parkiyo.repository.UserPassRepository;
import com.parkiyo.parkiyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPassService {

    private static final ZoneId COLOMBO = ZoneId.of("Asia/Colombo");

    private final UserPassRepository userPassRepository;
    private final PassPlanRepository passPlanRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public List<UserPass> listForUser(String email) {
        return userPassRepository.findByUser_EmailOrderByPurchasedAtDesc(email);
    }

    @Transactional(readOnly = true)
    public Optional<UserPass> getCurrentActivePass(String email) {
        return userPassRepository.findFirstByUser_EmailAndStatusOrderByEndDateDesc(email, UserPassStatus.ACTIVE)
                .filter(up -> {
                    LocalDate today = LocalDate.now(COLOMBO);
                    return !today.isBefore(up.getStartDate()) && !today.isAfter(up.getEndDate());
                });
    }

    /**
     * Active passes on a calendar day for user, then zone-filtered best for exit fee.
     */
    @Transactional(readOnly = true)
    public Optional<UserPass> findBestApplicablePass(Long userId, ParkingSlot slot, LocalDate exitDay, BigDecimal baseFee) {
        List<UserPass> active = userPassRepository.findActiveForUserOnDay(userId, UserPassStatus.ACTIVE, exitDay);
        BigDecimal b = baseFee != null && baseFee.compareTo(BigDecimal.ZERO) > 0
                ? baseFee
                : BigDecimal.valueOf(999999);
        return ParkingPassBenefitService.pickBestBenefit(active, slot, b);
    }

    @Transactional
    public void purchasePlan(String email, Long planId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userPassRepository.existsByUser_IdAndStatus(user.getId(), UserPassStatus.ACTIVE)) {
            throw new IllegalStateException(
                    "You already have an active parking pass. Cancel it or wait until it expires before buying another.");
        }

        PassPlan plan = passPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Pass plan not found"));
        if (!plan.isActive()) {
            throw new IllegalStateException("This pass is no longer available.");
        }

        BigDecimal price = plan.getPrice();
        walletService.deductBalance(email, price,
                "Parking pass purchase: " + plan.getName());

        LocalDate start = LocalDate.now(COLOMBO);
        LocalDate end = start.plusDays(plan.getDurationDays()).minusDays(1);

        UserPass userPass = UserPass.builder()
                .user(user)
                .passPlan(plan)
                .status(UserPassStatus.ACTIVE)
                .startDate(start)
                .endDate(end)
                .build();

        userPassRepository.save(userPass);

        notificationService.createNotification(
                user,
                NotificationType.SYSTEM,
                "Parking pass activated",
                plan.getName() + " is active until " + end + ".",
                "/passes"
        );
    }

    @Transactional
    public void cancelOwnActivePass(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserPass active = userPassRepository.findFirstByUser_EmailAndStatusOrderByEndDateDesc(email, UserPassStatus.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active pass to cancel"));

        LocalDate today = LocalDate.now(COLOMBO);
        if (today.isAfter(active.getEndDate())) {
            throw new IllegalStateException("This pass has already expired.");
        }

        active.setStatus(UserPassStatus.CANCELLED);
        userPassRepository.save(active);

        notificationService.createNotification(
                user,
                NotificationType.SYSTEM,
                "Parking pass cancelled",
                "Your " + active.getPassPlan().getName() + " pass was cancelled.",
                "/passes"
        );
    }

    @Transactional
    public int expirePassesEndedBefore(LocalDate today) {
        List<UserPass> expired = userPassRepository.findByStatusAndEndDateBefore(UserPassStatus.ACTIVE, today);
        for (UserPass up : expired) {
            up.setStatus(UserPassStatus.EXPIRED);
        }
        userPassRepository.saveAll(expired);
        return expired.size();
    }
}
