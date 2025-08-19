package com.jiralite.backend.controller;

import com.jiralite.backend.dto.RegisterRequest;
import com.jiralite.backend.dto.ApiResponse;
import com.jiralite.backend.model.User;
import com.jiralite.backend.model.Organization;
import com.jiralite.backend.repository.OrganizationRepository;
import com.jiralite.backend.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            Optional<Organization> orgOpt=organizationRepository.findById((request.getOrgId()));
            if(orgOpt.isEmpty()){
                return new ApiResponse<>("N","Organization Not found",null);
            }
            // Encrypt password
            String encodedPassword = passwordEncoder.encode(request.getPassword());

            User user = new User();
            user.setOrganization(orgOpt.get());
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
