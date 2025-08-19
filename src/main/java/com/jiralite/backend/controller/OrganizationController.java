package com.jiralite.backend.controller;

import com.jiralite.backend.dto.ApiResponse;
import com.jiralite.backend.model.Organization;
import com.jiralite.backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orgs")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    @PostMapping("/create")
    public ApiResponse<Organization> createOrganization(@RequestParam String name) {
        if (organizationRepository.existsByName(name)) {
            return new ApiResponse<>("N", "Organization name already exists", null);
        }
        Organization org = new Organization(name);
        return new ApiResponse<>("Y", "Organization created successfully", organizationRepository.save(org));
    }
}
