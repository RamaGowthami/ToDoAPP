package com.example.todoapp.controller;

import com.example.todoapp.dto.TaskResponseDTO;
import com.example.todoapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web Controller for serving HTML pages.
 * Provides web interface for the ToDo application.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

    private final TaskService taskService;

    /**
     * Home page - displays all tasks with pagination.
     */
    @GetMapping({"/", "/index", "/home"})
    public String home(@RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size,
                      Model model) {
        log.info("Loading home page with pagination: page={}, size={}", page, size);
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TaskResponseDTO> tasks = taskService.getAllTasks(pageable);
            
            model.addAttribute("tasks", tasks.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", tasks.getTotalPages());
            model.addAttribute("totalItems", tasks.getTotalElements());
            model.addAttribute("hasNext", tasks.hasNext());
            model.addAttribute("hasPrevious", tasks.hasPrevious());
            
            return "index";
        } catch (Exception e) {
            log.error("Error loading home page: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load tasks. Please try again.");
            return "index";
        }
    }

    /**
     * Task detail page - displays a specific task.
     */
    @GetMapping("/tasks/{id}")
    public String taskDetail(@PathVariable Long id, Model model) {
        log.info("Loading task detail page for task id: {}", id);
        
        try {
            TaskResponseDTO task = taskService.getTaskById(id);
            model.addAttribute("task", task);
            return "task-detail";
        } catch (Exception e) {
            log.error("Error loading task detail for id {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Task not found or error occurred.");
            return "redirect:/";
        }
    }

    /**
     * Create task page - form to create a new task.
     */
    @GetMapping("/tasks/new")
    public String createTaskForm(Model model) {
        log.info("Loading create task form");
        return "create-task";
    }

    /**
     * Edit task page - form to edit an existing task.
     */
    @GetMapping("/tasks/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        log.info("Loading edit task form for task id: {}", id);
        
        try {
            TaskResponseDTO task = taskService.getTaskById(id);
            model.addAttribute("task", task);
            return "edit-task";
        } catch (Exception e) {
            log.error("Error loading edit form for task id {}: {}", id, e.getMessage(), e);
            model.addAttribute("error", "Task not found or error occurred.");
            return "redirect:/";
        }
    }
}
