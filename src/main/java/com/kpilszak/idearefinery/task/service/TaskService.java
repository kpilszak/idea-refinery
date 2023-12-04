package com.kpilszak.idearefinery.task.service;

import com.kpilszak.idearefinery.task.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    public List<Task> getAllTasks() {
        return this.tasks;
    }

    public Optional<Task> getTaskById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return tasks.stream()
                    .filter(task -> uuid.equals(task.getId()))
                    .findFirst();
        } catch (IllegalArgumentException e) {
            log.warn("Provided id {} is not valid UUID", id);
            return Optional.empty();
        }
    }

    public Task createTask(Task task) {
        Task newTask = Task.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
        tasks.add(newTask);
        log.info("New task created: {}", newTask);
        return newTask;
    }

    public Optional<Task> updateTask(String id, Task task) {
        return getTaskById(id).map(existingTask -> {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setCompleted(task.getCompleted());
            existingTask.setCreateDate(task.getCreateDate());
            existingTask.setCompletedDate(task.getCompletedDate());

            log.info("Task updated: {}", existingTask);
            return existingTask;
        });
    }

}
