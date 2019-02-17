package com.example.javaproject.services;

import com.example.javaproject.models.Project;

import java.util.List;

public interface ProjectService {
    Project findById(Long id);
    List<Project> getAll();
    void save(Project task);
}
