package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Task.TaskStatus;
import com.example.taskmanagement.model.Task.TaskPriority;
import com.example.taskmanagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    
    private final TaskRepository taskRepository;
    
    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    // Get task by ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
    
    // Create new task
    @Transactional
    public Task createTask(Task task) {
        // Set default values if not provided
        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.TODO);
        }
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }
        if (task.getCompleted() == null) {
            task.setCompleted(false);
        }
        return taskRepository.save(task);
    }
    
    // Update existing task
    @Transactional
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        
        // Update fields
        if (taskDetails.getTitle() != null) {
            task.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            task.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getStatus() != null) {
            task.setStatus(taskDetails.getStatus());
            // Auto-complete if status is COMPLETED
            if (taskDetails.getStatus() == TaskStatus.COMPLETED && !task.getCompleted()) {
                task.markAsCompleted();
            }
        }
        if (taskDetails.getPriority() != null) {
            task.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getDueDate() != null) {
            task.setDueDate(taskDetails.getDueDate());
        }
        if (taskDetails.getCategory() != null) {
            task.setCategory(taskDetails.getCategory());
        }
        if (taskDetails.getAssignedTo() != null) {
            task.setAssignedTo(taskDetails.getAssignedTo());
        }
        
        return taskRepository.save(task);
    }
    
    // Delete task
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }
    
    // Mark task as completed
    @Transactional
    public Task markAsCompleted(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.markAsCompleted();
        return taskRepository.save(task);
    }
    
    // Get tasks by status
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }
    
    // Get tasks by priority
    public List<Task> getTasksByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }
    
    // Search tasks by title
    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
    // Get overdue tasks
    public List<Task> getOverdueTasks() {
        return taskRepository.findOverdueTasks(LocalDateTime.now());
    }
    
    // Get tasks due today
    public List<Task> getTasksDueToday() {
        return taskRepository.findTasksDueToday(LocalDateTime.now());
    }
    
    // Get completed tasks
    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompletedTrue();
    }
    
    // Get incomplete tasks
    public List<Task> getIncompleteTasks() {
        return taskRepository.findByCompletedFalse();
    }
    
    // Get tasks with filters
    public List<Task> getTasksWithFilters(
        TaskStatus status, 
        TaskPriority priority, 
        String category, 
        Boolean completed
    ) {
        return taskRepository.findTasksWithFilters(status, priority, category, completed);
    }
    
    // Get task statistics
    public TaskStatistics getTaskStatistics() {
        long total = taskRepository.count();
        long completed = taskRepository.countByCompletedTrue();
        long todo = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgress = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long overdue = taskRepository.findOverdueTasks(LocalDateTime.now()).size();
        
        return new TaskStatistics(total, completed, todo, inProgress, overdue);
    }
    
    // Inner class for statistics
    public record TaskStatistics(
        long total,
        long completed,
        long todo,
        long inProgress,
        long overdue
    ) {}
}