package com.jiralite.backend.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiralite.backend.model.Organization;
import com.jiralite.backend.model.Project;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.OrganizationRepository;
import com.jiralite.backend.repository.ProjectRepository;
import com.jiralite.backend.repository.UserRepository;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @GetMapping("/org/{orgId}")
    public Object getOrganizationDetails(@PathVariable UUID orgId) {
        return organizationRepository.findById(orgId)
                .map(org -> new OrgResponse("Y", "Organization found", org.getName()))
                .orElse(new OrgResponse("N", "Organization not found", null));
    }

    static class OrgResponse {
        private String status;
        private String message;
        private String orgName;

        public OrgResponse(String status, String message, String orgName) {
            this.status = status;
            this.message = message;
            this.orgName = orgName;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public String getOrgName() { return orgName; }
    }


    @GetMapping("/projects/{userId}")
    public ResponseEntity<?> getUserProjects(@PathVariable UUID userId) {
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "N",
                "error", "User not found"
            ));
        }

        User user = userOpt.get();

        UUID orgId = user.getId();

        Optional<Organization> orgOpt = organizationRepository.findById(orgId);
        if (orgOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "N",
                "error", "Organization not found"
            ));
        }

        List<Project> projects = projectRepository.findByOrganizationId(orgId);

        return ResponseEntity.ok(Map.of(
            "status", "Y",
            "organization", orgOpt.get().getName(),
            "projects", projects
        ));
    }
}
