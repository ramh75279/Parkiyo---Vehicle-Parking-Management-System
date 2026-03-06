package com.parkiyo.parkiyo.service;

import com.parkiyo.parkiyo.model.User;
import com.parkiyo.parkiyo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already registered";
        }

        userRepository.save(user);

        return "success";
    }
}