package com.jiralite.backend.repository;

import com.jiralite.backend.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {
    List<ProjectMember> findByProjectId(UUID projectId);
}
