package com.example.javaproject.services;

import com.example.javaproject.models.Task;

import java.util.List;

public interface TaskService {
    Task findById(Long id);
    List<Task> getAll();
}
