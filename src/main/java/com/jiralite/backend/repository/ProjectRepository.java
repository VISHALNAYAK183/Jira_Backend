package com.jiralite.backend.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jiralite.backend.model.Project;


@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByOrganizationId(UUID orgId);
}
