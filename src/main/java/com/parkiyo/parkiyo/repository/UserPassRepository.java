package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.enums.UserPassStatus;
import com.parkiyo.parkiyo.model.UserPass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserPassRepository extends JpaRepository<UserPass, Long> {

    boolean existsByUser_IdAndStatus(Long userId, UserPassStatus status);

    @EntityGraph(attributePaths = {"passPlan"})
    List<UserPass> findByUser_EmailOrderByPurchasedAtDesc(String email);

    @Query("SELECT up FROM UserPass up JOIN FETCH up.passPlan p WHERE up.user.id = :uid AND up.status = :status "
            + "AND :day BETWEEN up.startDate AND up.endDate")
    List<UserPass> findActiveForUserOnDay(@Param("uid") Long userId,
                                          @Param("status") UserPassStatus status,
                                          @Param("day") LocalDate day);

    List<UserPass> findByStatusAndEndDateBefore(UserPassStatus status, LocalDate date);

    @EntityGraph(attributePaths = {"passPlan"})
    Optional<UserPass> findFirstByUser_EmailAndStatusOrderByEndDateDesc(String email, UserPassStatus status);
}
