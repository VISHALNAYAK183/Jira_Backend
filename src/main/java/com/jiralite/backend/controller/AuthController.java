package com.jiralite.backend.controller;

import com.jiralite.backend.dto.RegisterRequest;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setOrgId(request.getOrgId());
        user.setEmail(request.getEmail());
        user.setPasswordHash(encodedPassword);
        user.setFullName(request.getFullName());
        user.setDesignation(request.getDesignation());
        user.setIsActive(true);

        return userRepository.save(user);
    }
}
