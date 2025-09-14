package com.jiralite.backend.service;

import com.jiralite.backend.model.Task;
import com.jiralite.backend.model.TaskStatus;
import com.jiralite.backend.model.User;
import com.jiralite.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

   public Task updateTaskStatus(UUID taskId, String status, User user) {
        Task task = taskRepository.findById(taskId).orElseThrow();

        if (!task.getAssignedTo().getId().equals(user.getId())) {
            throw new RuntimeException("Only the assigned user can update this task");
        }

        TaskStatus newStatus = TaskStatus.valueOf(status.toUpperCase());
        task.setStatus(newStatus);

        return taskRepository.save(task);
    }

    public Task getTask(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
