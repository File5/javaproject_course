package com.example.javaproject.controllers;

import com.example.javaproject.models.Project;
import com.example.javaproject.models.Task;
import com.example.javaproject.models.User;
import com.example.javaproject.services.ProjectService;
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
    private final ProjectService projectService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, ProjectService projectService) {
        this.taskService = taskService;
        this.userService = userService;
        this.projectService = projectService;
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
        Task existingTask = taskService.findById(id);
        task.setAssignedTo(existingTask.getAssignedTo());
        task.setProject(existingTask.getProject());
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
        model.addAttribute("nullable", true);
        model.addAttribute("task", task);
        model.addAttribute("values", values);
        model.addAttribute("checkedIndex", selectedIndex);
        return "tasks/task_edit_assignedTo";
    }

    @RequestMapping(value = {"/tasks/{id}/editAssignedTo"}, method = RequestMethod.POST)
    public String saveTask(Model model, @PathVariable("id") Long id, @RequestParam("radioAssignedTo") Long assignedToId) {
        Task task = taskService.findById(id);

        if (assignedToId != null) {
            User user = userService.findById(assignedToId);
            task.setAssignedTo(user);
        } else {
            task.setAssignedTo(null);
        }
        taskService.save(task);

        return "redirect:/tasks";
    }

    @RequestMapping(value = {"/tasks/{id}/editProject"}, method = RequestMethod.GET)
    public String getEditProjectForm(Model model, @PathVariable("id") Long id) {
        Task task = taskService.findById(id);
        List<Project> projects = projectService.getAll();
        Map<Long, Project> values = projects.stream().collect(Collectors.toMap(
                Project::getId,
                Function.identity(),
                (e1, e2) -> e2,
                LinkedHashMap::new
        ));
        Long selectedId = task.getProject() != null ? task.getProject().getId() : null;
        int selectedIndex = -1;
        int i = 0;
        for (Project project : projects) {
            if (Objects.equals(project.getId(), selectedId)) {
                selectedIndex = i;
            }
            i++;
        }
        model.addAttribute("create", false);
        model.addAttribute("nullable", true);
        model.addAttribute("task", task);
        model.addAttribute("values", values);
        model.addAttribute("checkedIndex", selectedIndex);
        return "tasks/task_edit_project";
    }

    @RequestMapping(value = {"/tasks/{id}/editProject"}, method = RequestMethod.POST)
    public String saveTaskProject(Model model, @PathVariable("id") Long id, @RequestParam("radioProject") Long projectId) {
        Task task = taskService.findById(id);

        if (projectId != null) {
            Project project = projectService.findById(projectId);
            task.setProject(project);
        } else {
            task.setProject(null);
        }
        taskService.save(task);

        return "redirect:/tasks";
    }
}
