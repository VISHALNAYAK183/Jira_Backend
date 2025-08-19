package com.jiralite.backend.repository;

import com.jiralite.backend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    boolean existsByName(String name);
}
