package com.example.javaproject.services;

import com.example.javaproject.data.TaskRepository;
import com.example.javaproject.models.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElse(null);
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public void save(Task task) {
        if (task.getId() != null) {
            Optional<Task> existingTask = taskRepository.findById(task.getId());

            task.setCreatedAt(existingTask.isPresent() ? existingTask.get().getCreatedAt() : new Date());
        } else {
            task.setCreatedAt(new Date());
        }
        taskRepository.saveAndFlush(task);
    }
}
