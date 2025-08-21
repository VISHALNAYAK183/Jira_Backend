package com.jiralite.backend.service;

import com.jiralite.backend.dto.ProjectRequest;
import com.jiralite.backend.model.Organization;
import com.jiralite.backend.model.Project;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.OrganizationRepository;
import com.jiralite.backend.repository.ProjectRepository;
import com.jiralite.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    public Project addProject(ProjectRequest request, UUID creatorId) {
        // Validate org exists
        Optional<Organization> orgOpt = organizationRepository.findById(request.getOrgId());
        if (orgOpt.isEmpty()) {
            throw new RuntimeException("Organization not found");
        }

        // Validate manager exists
        Optional<User> managerOpt = userRepository.findById(request.getManagerId());
        if (managerOpt.isEmpty()) {
            throw new RuntimeException("Manager not found");
        }

        User manager = managerOpt.get();

        // Create project
        Project project = new Project();
        project.setOrganization(orgOpt.get());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());
        project.setManager(manager);

        return projectRepository.save(project);
    }
}
