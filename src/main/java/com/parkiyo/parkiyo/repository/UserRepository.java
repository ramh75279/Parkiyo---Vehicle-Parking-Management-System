package com.parkiyo.parkiyo.repository;

import com.parkiyo.parkiyo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}