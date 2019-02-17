package com.example.javaproject.controllers;

import com.example.javaproject.models.Task;
import com.example.javaproject.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = {"/tasks"}, method = RequestMethod.GET)
    public String listOfTasks(Model model) {
        List<Task> tasks = taskService.getAll();
        model.addAttribute("tasks", tasks);
        return "tasks/task_list";
    }

    @RequestMapping(value = {"/tasks/{id}/edit"}, method = RequestMethod.GET)
    public String getEditTaskForm(Model model, @PathVariable("id") Long id) {
        Task task = taskService.findById(id);
        model.addAttribute("create", false);
        model.addAttribute("task", task);
        return "tasks/task_edit";
    }

    @RequestMapping(value = {"/tasks/{id}/edit"}, method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("task") Task task, BindingResult bindingResult, Model model, @PathVariable("id") Long id) {
        task.setId(id);
        taskService.save(task);
        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/tasks/new"}, method = RequestMethod.GET)
    public String getNewTaskForm(Model model) {
        model.addAttribute("create", true);
        model.addAttribute("task", new Task());
        return "tasks/task_edit";
    }

    @RequestMapping(value = {"/tasks/new"}, method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("task") Task task, BindingResult bindingResult, Model model) {
        taskService.save(task);
        return "redirect:/tasks";
    }
}
