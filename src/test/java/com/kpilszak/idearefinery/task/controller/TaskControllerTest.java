package com.kpilszak.idearefinery.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpilszak.idearefinery.task.model.Task;
import com.kpilszak.idearefinery.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private List<Task> tasks = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Task task1 = Task.builder()
                .title("Task no 1")
                .description("First task to be done")
                .build();
        Task task2 = Task.builder()
                .title("Task no 2")
                .description("Second task to be done")
                .build();
        tasks.addAll(List.of(task1, task2));
    }

    @Test
    void contextLoads() {
        assertNotNull(mockMvc, "MockMvc bean should be initialized");
        assertNotNull(objectMapper, "ObjectMapper bean should be initialized");
    }

    @Test
    void getAllTasks_ReturnsTasksList() throws Exception {
        when(taskService.getAllTasks())
                .thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andReturn();
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() throws Exception {
        Task task = tasks.get(0);
        String existingId = task.getId().toString();

        when(taskService.getTaskById(existingId))
                .thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", existingId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.title").value(task.getTitle()));
    }

    @Test
    void getTaskById_NonExistingId_ReturnsNotFound() throws Exception {
        String nonExistingId = UUID.randomUUID().toString();

        when(taskService.getTaskById(nonExistingId))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{id}", nonExistingId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTask_ValidTask_ReturnsCreatedTask() throws Exception {
        Task newTask = Task.builder()
                .title("Task no 3")
                .description("Third task to be done")
                .build();

        when(taskService.createTask(any(Task.class)))
                .thenReturn(newTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.title").value(newTask.getTitle()))
                .andExpect(jsonPath("$.description").value(newTask.getDescription()))
                .andDo(print());
    }

    @Test
    void updateTask_ExistingIdAndValidTask_ReturnsUpdatedTask() throws Exception {
        Task task = tasks.get(0);
        String existingId = task.getId().toString();
        Task taskWithUpdates = task.toBuilder()
                .completed(true)
                .build();

        when(taskService.updateTask(any(String.class), any(Task.class)))
                .thenReturn(Optional.of(taskWithUpdates));

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskWithUpdates)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.title").value(task.getTitle()))
                .andExpect(jsonPath("$.description").value(task.getDescription()))
                .andExpect(jsonPath("$.completed").value(taskWithUpdates.getCompleted()))
                .andDo(print());
    }

    @Test
    public void updateTask_NonExistingId_ReturnsNotFound() throws Exception {
        String nonExistingId = UUID.randomUUID().toString();
        Task nonExistingTask = Task.builder()
                .title("Task title")
                .completed(true)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistingTask)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}