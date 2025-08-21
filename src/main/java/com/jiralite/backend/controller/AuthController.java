package com.jiralite.backend.controller;

import com.jiralite.backend.dto.RegisterRequest;
import com.jiralite.backend.dto.ApiResponse;
import com.jiralite.backend.dto.LoginRequest;
import com.jiralite.backend.model.User;
import com.jiralite.backend.security.JwtUtil;
import com.jiralite.backend.model.Organization;
import com.jiralite.backend.repository.OrganizationRepository;
import com.jiralite.backend.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private JwtUtil jwtUtil;  

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        try {
            Optional<Organization> orgOpt=organizationRepository.findById((request.getOrgId()));
            if(orgOpt.isEmpty()){
                return new ApiResponse<>("N","Organization Not found",null);
            }
            
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
           
            return new ApiResponse<>("N", "User with this email already exists in the organization", null);
        } catch (Exception ex) {
            
            return new ApiResponse<>("N", "Error occurred: " + ex.getMessage(), null);
        }
    }
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

    if (userOpt.isEmpty()) {
        return ResponseEntity.status(401).body(Map.of(
            "status", "N",
            "error", "Invalid email or password"
        ));
    }

    User user = userOpt.get();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {
        return ResponseEntity.status(401).body(Map.of(
            "status", "N",
            "error", "Invalid email or password"
        ));
    }

   
    String token = jwtUtil.generateToken(
    user.getEmail(),
    user.getOrganization().getId().toString(),
    user.getDesignation(),
    user.getFullName()
);


    return ResponseEntity.ok(Map.of(
        "status", "Y",
        "message", "Login successful",
        "token", token
    ));
}

}
