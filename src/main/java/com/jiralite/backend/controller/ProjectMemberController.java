package com.jiralite.backend.controller;

import com.jiralite.backend.model.*;
import com.jiralite.backend.repository.*;
import com.jiralite.backend.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/projects")
public class ProjectMemberController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private JwtUtil jwtUtil;
@PostMapping("/{projectId}/assign")
public ResponseEntity<?> assignMemberToProject(
        @PathVariable UUID projectId,
        @RequestParam UUID userId,
        @RequestParam String role,
        @RequestHeader("Authorization") String authHeader) {

    // 1. Extract manager info from token (optional: ensure only MANAGER can assign)
    String token = authHeader.replace("Bearer ", "");
    String designation = jwtUtil.extractDesignation(token);
    if (!"manager".equalsIgnoreCase(designation)) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
            "status", "N",
            "error", "Only managers can assign team members"
        ));
    }

    // 2. Get project
    Optional<Project> projectOpt = projectRepository.findById(projectId);
    if (projectOpt.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
            "status", "N",
            "error", "Project not found"
        ));
    }
    Project project = projectOpt.get();

    // 3. Get user
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of(
            "status", "N",
            "error", "User not found"
        ));
    }
    User user = userOpt.get();

    // 4. Validate that both belong to same organization
    if (!user.getOrganization().getId().equals(project.getOrganization().getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
            "status", "N",
            "error", "User does not belong to this organization"
        ));
    }

    // 5. Save in project_members
    ProjectMember member = new ProjectMember();
    member.setProject(project);
    member.setUser(user);
    member.setRole(role);
    projectMemberRepository.save(member);

    return ResponseEntity.ok(Map.of(
        "status", "Y",
        "message", "User assigned successfully",
        "projectId", projectId,
        "userId", userId,
        "role", role
    ));
}


    // 2️⃣ Get all members of a project
    @GetMapping("/{projectId}/members")
    public ResponseEntity<?> getProjectMembers(@PathVariable UUID projectId) {
        List<ProjectMember> members = projectMemberRepository.findByProjectId(projectId);

        if (members.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                "status", "N",
                "message", "No members assigned to this project",
                "members", List.of()
            ));
        }

        List<Map<String, Object>> memberList = new ArrayList<>();
        for (ProjectMember m : members) {
            memberList.add(Map.of(
                "memberId", m.getId(),
                "userId", m.getUser().getId(),
                "fullName", m.getUser().getFullName(),
                "designation", m.getUser().getDesignation(),
                "role", m.getRole()
            ));
        }

        return ResponseEntity.ok(Map.of(
            "status", "Y",
            "message", "Project members retrieved",
            "projectId", projectId,
            "members", memberList
        ));
    }
}
