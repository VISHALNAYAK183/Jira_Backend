package com.jiralite.backend.repository;

import com.jiralite.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface IssueRepository extends JpaRepository<Issue, UUID> {
    List<Issue> findByTaskId(UUID taskId);
}