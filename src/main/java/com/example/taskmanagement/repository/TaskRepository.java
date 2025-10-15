package com.example.taskmanagement.repository;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.Task.TaskStatus;
import com.example.taskmanagement.model.Task.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Find by status
    List<Task> findByStatus(TaskStatus status);
    
    // Find by priority
    List<Task> findByPriority(TaskPriority priority);
    
    // Find completed tasks
    List<Task> findByCompletedTrue();
    
    // Find incomplete tasks
    List<Task> findByCompletedFalse();
    
    // Find by category
    List<Task> findByCategory(String category);
    
    // Find by title containing (search)
    List<Task> findByTitleContainingIgnoreCase(String keyword);
    
    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :now AND t.completed = false")
    List<Task> findOverdueTasks(@Param("now") LocalDateTime now);
    
    // Find tasks due today
    @Query("SELECT t FROM Task t WHERE DATE(t.dueDate) = DATE(:today) AND t.completed = false")
    List<Task> findTasksDueToday(@Param("today") LocalDateTime today);
    
    // Find tasks by status and priority
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
    
    // Find tasks created between dates
    List<Task> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Count tasks by status
    long countByStatus(TaskStatus status);
    
    // Count completed tasks
    long countByCompletedTrue();
    
    // Get all tasks ordered by due date
    List<Task> findAllByOrderByDueDateAsc();
    
    // Get all tasks ordered by priority
    List<Task> findAllByOrderByPriorityDesc();
    
    // Custom query: Get tasks with specific filters
    @Query("SELECT t FROM Task t WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:category IS NULL OR t.category = :category) AND " +
           "(:completed IS NULL OR t.completed = :completed)")
    List<Task> findTasksWithFilters(
        @Param("status") TaskStatus status,
        @Param("priority") TaskPriority priority,
        @Param("category") String category,
        @Param("completed") Boolean completed
    );
}