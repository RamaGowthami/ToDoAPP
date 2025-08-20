package com.example.todoapp.controller;

import com.example.todoapp.dto.ApiResponse;
import com.example.todoapp.dto.TaskRequestDTO;
import com.example.todoapp.dto.TaskResponseDTO;
import com.example.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing ToDo tasks.
 * 
 * Provides CRUD operations for tasks with pagination support.
 * All endpoints return standardized API responses wrapped in ApiResponse format.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {

    private final TaskService taskService;

    /**
     * Create a new task.
     * 
     * @param taskRequestDTO The task data (title is required, description is optional)
     * @return Created task with generated ID and timestamps
     * 
     * Example request:
     * {
     *   "title": "Complete project documentation",
     *   "description": "Write comprehensive documentation for the Spring Boot project",
     *   "completed": false
     * }
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDTO>> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        log.info("Received request to create task: {}", taskRequestDTO.getTitle());
        
        TaskResponseDTO createdTask = taskService.createTask(taskRequestDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", createdTask));
    }

    /**
     * Get all tasks with pagination support.
     * 
     * @param page Page number (0-based, default: 0)
     * @param size Page size (default: 10)
     * @return Paginated list of tasks
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDTO>>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        log.info("Received request to get all tasks with pagination: page={}, size={}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<TaskResponseDTO> tasks = taskService.getAllTasks(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved successfully", tasks));
    }

    /**
     * Get a specific task by ID.
     * 
     * @param id Task ID
     * @return Task details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> getTaskById(@PathVariable Long id) {
        log.info("Received request to get task with id: {}", id);
        
        TaskResponseDTO task = taskService.getTaskById(id);
        
        return ResponseEntity.ok(ApiResponse.success("Task retrieved successfully", task));
    }

    /**
     * Update an existing task.
     * 
     * @param id Task ID
     * @param taskRequestDTO Updated task data (title is required, description is optional)
     * @return Updated task details
     * 
     * Example request:
     * {
     *   "title": "Updated task title",
     *   "description": "Updated task description",
     *   "completed": true
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        
        log.info("Received request to update task with id: {}", id);
        
        TaskResponseDTO updatedTask = taskService.updateTask(id, taskRequestDTO);
        
        return ResponseEntity.ok(ApiResponse.success("Task updated successfully", updatedTask));
    }

    /**
     * Delete a task by ID.
     * 
     * @param id Task ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTask(@PathVariable Long id) {
        log.info("Received request to delete task with id: {}", id);
        
        taskService.deleteTask(id);
        
        return ResponseEntity.ok(ApiResponse.success("Task deleted successfully"));
    }

    /**
     * Mark a task as completed.
     * 
     * @param id Task ID
     * @return Updated task with completed status
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponseDTO>> completeTask(@PathVariable Long id) {
        log.info("Received request to complete task with id: {}", id);
        
        TaskResponseDTO completedTask = taskService.completeTask(id);
        
        return ResponseEntity.ok(ApiResponse.success("Task marked as completed", completedTask));
    }
}
