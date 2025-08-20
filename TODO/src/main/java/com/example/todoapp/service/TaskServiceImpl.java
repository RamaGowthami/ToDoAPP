package com.example.todoapp.service;

import com.example.todoapp.dto.TaskRequestDTO;
import com.example.todoapp.dto.TaskResponseDTO;
import com.example.todoapp.entity.Task;
import com.example.todoapp.exception.ResourceNotFoundException;
import com.example.todoapp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        log.info("Creating new task with title: {}", taskRequestDTO.getTitle());

        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        task.setCompleted(taskRequestDTO.getCompleted() != null ? taskRequestDTO.getCompleted() : false);

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        return mapToResponseDTO(savedTask);
    }

    @Override
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        log.info("Fetching tasks with pagination: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Task> tasks = taskRepository.findAll(pageable);
        Page<TaskResponseDTO> responseDTOs = tasks.map(this::mapToResponseDTO);

        log.info("Found {} tasks", tasks.getTotalElements());
        return responseDTOs;
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        log.info("Fetching task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        log.info("Task found with id: {}", id);
        return mapToResponseDTO(task);
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        log.info("Updating task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.setTitle(taskRequestDTO.getTitle());
        if (taskRequestDTO.getDescription() != null) {
            task.setDescription(taskRequestDTO.getDescription());
        }
        if (taskRequestDTO.getCompleted() != null) {
            task.setCompleted(taskRequestDTO.getCompleted());
        }

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", id);

        return mapToResponseDTO(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);

        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with id: {}", id);
    }

    @Override
    public TaskResponseDTO completeTask(Long id) {
        log.info("Marking task as completed with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));

        task.setCompleted(true);
        Task updatedTask = taskRepository.save(task);

        log.info("Task marked as completed with id: {}", id);
        return mapToResponseDTO(updatedTask);
    }

    private TaskResponseDTO mapToResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.getCompleted());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());
        return dto;
    }
}
