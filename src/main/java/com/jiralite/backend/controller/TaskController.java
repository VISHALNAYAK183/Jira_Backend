package com.jiralite.backend.controller;

import com.jiralite.backend.model.*;
import com.jiralite.backend.repository.*;
import com.jiralite.backend.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private ProjectRepository projectRepo;
    @Autowired private UserRepository userRepo;
        @Autowired
    private TaskService taskService;

    // Create Task (by Manager)
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Map<String, Object> req) {
        UUID projectId = UUID.fromString(req.get("projectId").toString());
        UUID assignedTo = UUID.fromString(req.get("assignedTo").toString());
        UUID createdBy = UUID.fromString(req.get("createdBy").toString());

        Project project = projectRepo.findById(projectId).orElseThrow();
        User assignee = userRepo.findById(assignedTo).orElseThrow();
        User creator = userRepo.findById(createdBy).orElseThrow();

        Task task = new Task();
        task.setProject(project);
        task.setAssignedTo(assignee);
        task.setCreatedBy(creator);
        task.setTitle(req.get("title").toString());
        task.setDescription(req.get("description").toString());

        taskRepo.save(task);

        return ResponseEntity.ok(Map.of("status", "Y", "taskId", task.getId()));
    }

    // Get tasks by project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(taskRepo.findByProjectId(projectId));
    }

    // Update task status
    @PutMapping("/{taskId}/status")
    public ResponseEntity<Task> updateStatus(
            @PathVariable UUID taskId,
            @RequestParam String status
    ) {
        Task updated = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updated);
    }
    
}
