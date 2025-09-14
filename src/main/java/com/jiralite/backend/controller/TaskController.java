package com.jiralite.backend.controller;

import com.jiralite.backend.model.*;
import com.jiralite.backend.repository.*;
import com.jiralite.backend.service.TaskService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    @Autowired private TaskRepository taskRepo;
    @Autowired private ProjectRepository projectRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private TaskService taskService;

    
   @PostMapping("/add")
public ResponseEntity<?> createTask(@RequestBody Map<String, Object> req) {
    try {
        if (!req.containsKey("projectId") || !req.containsKey("assignedTo") ||
            !req.containsKey("createdBy") || !req.containsKey("title") ||
            !req.containsKey("description")) {
            return ResponseEntity.badRequest().body(
                Map.of("status", "N", "message", "Missing required fields")
            );
        }

        UUID projectId = UUID.fromString(req.get("projectId").toString());
        UUID assignedTo = UUID.fromString(req.get("assignedTo").toString());
        UUID createdBy = UUID.fromString(req.get("createdBy").toString());

        Project project = projectRepo.findById(projectId).orElseThrow();
        User assignee = userRepo.findById(assignedTo).orElseThrow();
        User creator = userRepo.findById(createdBy).orElseThrow();

        String role = creator.getDesignation().toUpperCase();
        if (!(role.equals("MANAGER") || role.equals("ORGADMIN"))) {
            return ResponseEntity.status(403).body(
                Map.of("status", "N", "message", "Only Manager or OrgAdmin can create tasks")
            );
        }

        Task task = new Task();
        task.setProject(project);
        task.setAssignedTo(assignee);
        task.setCreatedBy(creator);
        task.setTitle(req.get("title").toString());
        task.setDescription(req.get("description").toString());
        task.setStatus(TaskStatus.PENDING);

        taskRepo.save(task);

        return ResponseEntity.ok(Map.of(
            "status", "Y",
            "taskId", task.getId(),
            "message", "Task created successfully"
        ));

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(
            Map.of("status", "N", "message", "Error: " + e.getMessage())
        );
    }
}

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(taskRepo.findByProjectId(projectId));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable UUID taskId,
            @RequestParam String status,
            @RequestParam UUID userId 
    ) {
        User user = userRepo.findById(userId).orElseThrow();

        Task updated = taskService.updateTaskStatus(taskId, status, user);
        return ResponseEntity.ok(Map.of(
                "status", "Y",
                "taskId", updated.getId(),
                "newStatus", updated.getStatus()
        ));
    }
}
