package com.example.todoapp.controller;

import com.example.todoapp.dto.TaskRequestDTO;
import com.example.todoapp.dto.TaskResponseDTO;
import com.example.todoapp.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskRequestDTO taskRequestDTO;
    private TaskResponseDTO taskResponseDTO;

    @BeforeEach
    void setUp() {
        taskRequestDTO = TaskRequestDTO.builder()
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .build();

        taskResponseDTO = TaskResponseDTO.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createTask_Success() throws Exception {
        when(taskService.createTask(any(TaskRequestDTO.class))).thenReturn(taskResponseDTO);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test Task"));
    }

    @Test
    void createTask_ValidationError_EmptyTitle() throws Exception {
        TaskRequestDTO invalidRequest = TaskRequestDTO.builder()
                .title("")
                .description("Test Description")
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed. Please check the following fields:"))
                .andExpect(jsonPath("$.data.title").value("Title is required and cannot be empty"));
    }

    @Test
    void createTask_ValidationError_TitleTooLong() throws Exception {
        String longTitle = "a".repeat(256);
        TaskRequestDTO invalidRequest = TaskRequestDTO.builder()
                .title(longTitle)
                .description("Test Description")
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.data.title").value("Title must be between 1 and 255 characters"));
    }

    @Test
    void createTask_ValidationError_DescriptionTooLong() throws Exception {
        String longDescription = "a".repeat(1001);
        TaskRequestDTO invalidRequest = TaskRequestDTO.builder()
                .title("Test Task")
                .description(longDescription)
                .build();

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.data.description").value("Description cannot exceed 1000 characters"));
    }

    @Test
    void getAllTasks_Success() throws Exception {
        Page<TaskResponseDTO> taskPage = new PageImpl<>(List.of(taskResponseDTO));
        when(taskService.getAllTasks(any(PageRequest.class))).thenReturn(taskPage);

        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Tasks retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1));
    }

    @Test
    void getTaskById_Success() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(taskResponseDTO);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void updateTask_Success() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskRequestDTO.class))).thenReturn(taskResponseDTO);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task updated successfully"))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void updateTask_ValidationError() throws Exception {
        TaskRequestDTO invalidRequest = TaskRequestDTO.builder()
                .title("")
                .description("Test Description")
                .build();

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.data.title").value("Title is required and cannot be empty"));
    }

    @Test
    void deleteTask_Success() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task deleted successfully"));
    }

    @Test
    void completeTask_Success() throws Exception {
        TaskResponseDTO completedTask = TaskResponseDTO.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .completed(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(taskService.completeTask(1L)).thenReturn(completedTask);

        mockMvc.perform(patch("/api/tasks/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Task marked as completed"))
                .andExpect(jsonPath("$.data.completed").value(true));
    }
}
