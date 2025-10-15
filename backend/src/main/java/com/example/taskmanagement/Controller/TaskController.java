package com.example.taskmanagement.Controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Task.TaskStatus;
import com.example.taskmanagement.model.Task.TaskPriority;
import com.example.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TaskController {
    
    private final TaskService taskService;
    
    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
        @RequestParam(required = false) TaskStatus status,
        @RequestParam(required = false) TaskPriority priority,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Boolean completed
    ) {
        List<Task> tasks;
        
        // If any filter is provided, use filtered search
        if (status != null || priority != null || category != null || completed != null) {
            tasks = taskService.getTasksWithFilters(status, priority, category, completed);
        } else {
            tasks = taskService.getAllTasks();
        }
        
        return ResponseEntity.ok(tasks);
    }
    
    // Get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Create new task
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }
    
    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
        @PathVariable Long id,
        @Valid @RequestBody Task task
    ) {
        try {
            Task updatedTask = taskService.updateTask(id, task);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok(Map.of(
                "message", "Task deleted successfully",
                "id", id.toString()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Mark task as completed
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markAsCompleted(@PathVariable Long id) {
        try {
            Task task = taskService.markAsCompleted(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Search tasks
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok(tasks);
    }
    
    // Get tasks by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        return ResponseEntity.ok(tasks);
    }
    
    // Get tasks by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable TaskPriority priority) {
        List<Task> tasks = taskService.getTasksByPriority(priority);
        return ResponseEntity.ok(tasks);
    }
    
    // Get overdue tasks
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdueTasks() {
        List<Task> tasks = taskService.getOverdueTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get tasks due today
    @GetMapping("/due-today")
    public ResponseEntity<List<Task>> getTasksDueToday() {
        List<Task> tasks = taskService.getTasksDueToday();
        return ResponseEntity.ok(tasks);
    }
    
    // Get completed tasks
    @GetMapping("/completed")
    public ResponseEntity<List<Task>> getCompletedTasks() {
        List<Task> tasks = taskService.getCompletedTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get incomplete tasks
    @GetMapping("/incomplete")
    public ResponseEntity<List<Task>> getIncompleteTasks() {
        List<Task> tasks = taskService.getIncompleteTasks();
        return ResponseEntity.ok(tasks);
    }
    
    // Get task statistics
    @GetMapping("/statistics")
    public ResponseEntity<TaskService.TaskStatistics> getStatistics() {
        TaskService.TaskStatistics stats = taskService.getTaskStatistics();
        return ResponseEntity.ok(stats);
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Task Management API",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}