package com.jiralite.backend.controller;

import com.jiralite.backend.dto.RegisterRequest;
import com.jiralite.backend.dto.ApiResponse;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            // Encrypt password
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            User user = new User();
            user.setOrgId(request.getOrgId());
            user.setEmail(request.getEmail().trim().toLowerCase());
            user.setPasswordHash(encodedPassword);
            user.setFullName(request.getFullName());
            user.setDesignation(request.getDesignation());
            user.setIsActive(true);

            User savedUser = userRepository.save(user);

            return new ApiResponse<>("Y", "User registered successfully", savedUser);

        } catch (DataIntegrityViolationException ex) {
            // handles unique constraint violation (duplicate email/org)
            return new ApiResponse<>("N", "User with this email already exists in the organization", null);
        } catch (Exception ex) {
            // handles any other unexpected error
            return new ApiResponse<>("N", "Error occurred: " + ex.getMessage(), null);
        }
    }
}
