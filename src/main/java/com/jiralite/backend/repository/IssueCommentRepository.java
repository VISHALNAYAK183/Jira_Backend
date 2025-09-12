package com.jiralite.backend.repository;

import com.jiralite.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface IssueCommentRepository extends JpaRepository<IssueComment, UUID> {
    List<IssueComment> findByIssueId(UUID issueId);
}