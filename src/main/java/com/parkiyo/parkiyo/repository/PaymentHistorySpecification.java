package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.PaymentStatus;
import com.parkiyo.parkiyo.model.ParkingRecord;
import com.parkiyo.parkiyo.model.Payment;
import com.parkiyo.parkiyo.model.Reservation;
import com.parkiyo.parkiyo.model.Vehicle;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public final class PaymentHistorySpecification {

    private PaymentHistorySpecification() {
    }

    public static Specification<Payment> forUserHistory(
            String email,
            String search,
            PaymentStatus status,
            String methodKey,
            LocalDate from,
            LocalDate to) {

        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            preds.add(cb.equal(root.join("user").get("email"), email));

            if (status != null) {
                preds.add(cb.equal(root.get("status"), status));
            }

            if (methodKey != null && !methodKey.isBlank() && !"ALL".equalsIgnoreCase(methodKey)) {
                Expression<String> pm = cb.lower(root.get("paymentMethod"));
                switch (methodKey.toUpperCase()) {
                    case "WALLET" -> preds.add(cb.like(pm, "%wallet%"));
                    case "CARD" -> preds.add(cb.or(
                            cb.like(pm, "%card%"),
                            cb.like(pm, "%credit%"),
                            cb.like(pm, "%tap%")));
                    case "CASH" -> preds.add(cb.equal(cb.upper(root.get("paymentMethod")), "CASH"));
                    default -> preds.add(cb.like(pm, "%" + methodKey.toLowerCase() + "%"));
                }
            }

            Expression<LocalDateTime> when = cb.coalesce(root.get("paidAt"), root.get("createdAt"));
            if (from != null) {
                preds.add(cb.greaterThanOrEqualTo(when, from.atStartOfDay()));
            }
            if (to != null) {
                preds.add(cb.lessThan(when, to.plusDays(1).atStartOfDay()));
            }

            if (search != null && !search.isBlank()) {
                String term = "%" + search.trim().toUpperCase() + "%";
                Join<Payment, ParkingRecord> pr = root.join("parkingRecord", JoinType.LEFT);
                Join<ParkingRecord, Vehicle> v1 = pr.join("vehicle", JoinType.LEFT);
                Join<Payment, Reservation> res = root.join("reservation", JoinType.LEFT);
                Join<Reservation, Vehicle> v2 = res.join("vehicle", JoinType.LEFT);
                preds.add(cb.or(
                        cb.like(cb.upper(root.get("transactionCode")), term),
                        cb.like(cb.upper(v1.get("licensePlate")), term),
                        cb.like(cb.upper(v2.get("licensePlate")), term)
                ));
                query.distinct(true);
            }

            return cb.and(preds.toArray(Predicate[]::new));
        };
    }
}
