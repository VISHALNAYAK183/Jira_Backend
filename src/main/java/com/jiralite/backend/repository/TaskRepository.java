package com.jiralite.backend.repository;

import com.jiralite.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByProjectId(UUID projectId);
    List<Task> findByAssignedToId(UUID userId);
}