package com.example.javaproject.controllers;

import com.example.javaproject.models.Task;
import com.example.javaproject.models.User;
import com.example.javaproject.services.TaskService;
import com.example.javaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
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

    @RequestMapping(value = {"/tasks/{id}/editAssignedTo"}, method = RequestMethod.GET)
    public String getEditAssignedToForm(Model model, @PathVariable("id") Long id) {
        Task task = taskService.findById(id);
        List<User> users = userService.getAll();
        Map<Long, User> values = users.stream().collect(Collectors.toMap(
                User::getId,
                Function.identity(),
                (e1, e2) -> e2,
                LinkedHashMap::new
        ));
        Long selectedId = task.getAssignedTo() != null ? task.getAssignedTo().getId() : null;
        int selectedIndex = -1;
        int i = 0;
        for (User user : users) {
            if (Objects.equals(user.getId(), selectedId)) {
                selectedIndex = i;
            }
            i++;
        }
        model.addAttribute("create", false);
        model.addAttribute("task", task);
        model.addAttribute("values", values);
        model.addAttribute("checkedIndex", selectedIndex);
        return "tasks/task_edit_assignedTo";
    }
}
