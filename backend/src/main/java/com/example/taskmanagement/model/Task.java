package com.example.taskmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.TODO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    // Optional: Category/Tag
    @Column(length = 50)
    private String category;
    
    // Optional: Assigned user (for future multi-user support)
    @Column(name = "assigned_to")
    private String assignedTo;
    
    // Enums
    public enum TaskStatus {
        TODO("To Do"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        TaskStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum TaskPriority {
        LOW("Low"),
        MEDIUM("Medium"),
        HIGH("High"),
        URGENT("Urgent");
        
        private final String displayName;
        
        TaskPriority(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Helper method to mark task as completed
    public void markAsCompleted() {
        this.completed = true;
        this.status = TaskStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
    
    // Helper method to check if task is overdue
    public boolean isOverdue() {
        if (dueDate == null || completed) {
            return false;
        }
        return LocalDateTime.now().isAfter(dueDate);
    }
}