package com.jiralite.backend.controller;

import com.jiralite.backend.model.*;
import com.jiralite.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/issues")
public class IssueController {

    @Autowired private IssueRepository issueRepo;
    @Autowired private IssueImageRepository imageRepo;
    @Autowired private IssueCommentRepository commentRepo;
    @Autowired private TaskRepository taskRepo;
    @Autowired private UserRepository userRepo;

    // Raise an issue
    @PostMapping
    public ResponseEntity<?> createIssue(@RequestBody Map<String, Object> req) {
        UUID taskId = UUID.fromString(req.get("taskId").toString());
        UUID createdBy = UUID.fromString(req.get("createdBy").toString());

        Task task = taskRepo.findById(taskId).orElseThrow();
        User creator = userRepo.findById(createdBy).orElseThrow();

        Issue issue = new Issue();
        issue.setTask(task);
        issue.setCreatedBy(creator);
        issue.setTitle(req.get("title").toString());
        issue.setDescription(req.get("description").toString());

        issueRepo.save(issue);
        return ResponseEntity.ok(Map.of("status", "Y", "issueId", issue.getId()));
    }

    // Add image to issue
    @PostMapping("/{issueId}/images")
    public ResponseEntity<?> addImage(@PathVariable UUID issueId, @RequestBody Map<String, String> req) {
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        IssueImage img = new IssueImage();
        img.setIssue(issue);
        img.setImageUrl(req.get("imageUrl")); // (later integrate file upload)
        imageRepo.save(img);
        return ResponseEntity.ok(Map.of("status", "Y", "message", "Image added"));
    }

    // Add comment
    @PostMapping("/{issueId}/comments")
    public ResponseEntity<?> addComment(@PathVariable UUID issueId, @RequestBody Map<String, String> req) {
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        User user = userRepo.findById(UUID.fromString(req.get("createdBy"))).orElseThrow();

        IssueComment comment = new IssueComment();
        comment.setIssue(issue);
        comment.setCreatedBy(user);
        comment.setMessage(req.get("message"));

        commentRepo.save(comment);
        return ResponseEntity.ok(Map.of("status", "Y", "message", "Comment added"));
    }

    // Get issues by task
    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getIssuesByTask(@PathVariable UUID taskId) {
        List<Issue> issues = issueRepo.findByTaskId(taskId);
        return ResponseEntity.ok(issues);
    }

    // Resolve issue
    @PatchMapping("/{issueId}/resolve")
    public ResponseEntity<?> resolveIssue(@PathVariable UUID issueId) {
        Issue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setStatus(IssueStatus.valueOf("RESOLVED"));

        issueRepo.save(issue);
        return ResponseEntity.ok(Map.of("status", "Y", "message", "Issue resolved"));
    }
}
