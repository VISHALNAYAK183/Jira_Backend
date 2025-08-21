package com.jiralite.backend.controller;

import com.jiralite.backend.dto.ProjectRequest;
import com.jiralite.backend.model.Project;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.UserRepository;
import com.jiralite.backend.security.JwtUtil;
import com.jiralite.backend.service.ProjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addProject(@RequestBody ProjectRequest request,
                                        @RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(jwt);

            // Get logged in user
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status", "N", "error", "User not found"));
            }

            User creator = userOpt.get();

            // Check if user is OrgAdmin
            if (!"OrgAdmin".equalsIgnoreCase(creator.getDesignation())) {
                return ResponseEntity.status(403).body(Map.of("status", "N", "error", "Only OrgAdmin can create projects"));
            }

            Project project = projectService.addProject(request, creator.getId());

            return ResponseEntity.ok(Map.of(
                    "status", "Y",
                    "message", "Project created successfully",
                    "projectId", project.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "N", "error", e.getMessage()));
        }
    }
}
