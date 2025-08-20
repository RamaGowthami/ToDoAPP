package com.example.todoapp.service;

import com.example.todoapp.dto.TaskRequestDTO;
import com.example.todoapp.dto.TaskResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);
    
    Page<TaskResponseDTO> getAllTasks(Pageable pageable);
    
    TaskResponseDTO getTaskById(Long id);
    
    TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO);
    
    void deleteTask(Long id);
    
    TaskResponseDTO completeTask(Long id);
}
