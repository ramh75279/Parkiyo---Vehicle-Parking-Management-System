package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.Vehicle;
import com.parkiyo.parkiyo.enums.VehicleCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);

    /** Use this for queries; {@code findByUserId} is ambiguous for a {@code user} association. */
    List<Vehicle> findByUser_Id(Long userId);

    List<Vehicle> findByUserEmail(String email);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM Vehicle v WHERE v.user IS NOT NULL AND v.user.id = :userId")
    void deleteAllForUser(@Param("userId") Long userId);

    List<Vehicle> findByCategory(VehicleCategory category);

    List<Vehicle> findByActiveTrue();

    List<Vehicle> findByLicensePlateContainingIgnoreCase(String licensePlate);

    @Query("SELECT DISTINCT v FROM Vehicle v "
            + "LEFT JOIN FETCH v.user u "
            + "LEFT JOIN FETCH u.wallet "
            + "WHERE v.id = :id")
    Optional<Vehicle> findByIdWithUserAndWallet(@Param("id") Long id);
}
