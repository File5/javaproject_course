package com.example.javaproject.controllers;

import com.example.javaproject.models.Project;
import com.example.javaproject.models.User;
import com.example.javaproject.services.ProjectService;
import com.example.javaproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/projects"}, method = RequestMethod.GET)
    public String listOfTasks(Model model) {
        List<Project> projects = projectService.getAll();
        model.addAttribute("projects", projects);
        return "projects/project_list";
    }

    @RequestMapping(value = {"/projects/{id}/edit"}, method = RequestMethod.GET)
    public String getEditTaskForm(Model model, @PathVariable("id") Long id) {
        Project project = projectService.findById(id);
        model.addAttribute("create", false);
        model.addAttribute("project", project);
        return "projects/project_edit";
    }

    @RequestMapping(value = {"/projects/{id}/edit"}, method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("project") Project project, BindingResult bindingResult, Model model, @PathVariable("id") Long id) {
        project.setId(id);
        Project existingProject = projectService.findById(id);
        project.setAssignedTo(existingProject.getAssignedTo());
        projectService.save(project);
        return "redirect:/projects";
    }

    @RequestMapping(value = {"/projects/new"}, method = RequestMethod.GET)
    public String getNewTaskForm(Model model) {
        model.addAttribute("create", true);
        model.addAttribute("project", new Project());
        return "projects/project_edit";
    }

    @RequestMapping(value = {"/projects/new"}, method = RequestMethod.POST)
    public String saveTask(@ModelAttribute("project") Project project, BindingResult bindingResult, Model model) {
        projectService.save(project);
        return "redirect:/projects";
    }

    @RequestMapping(value = {"/projects/{id}/editAssignedTo"}, method = RequestMethod.GET)
    public String getEditAssignedToForm(Model model, @PathVariable("id") Long id) {
        Project project = projectService.findById(id);
        List<User> users = userService.getAll();
        Map<Long, User> values = users.stream().collect(Collectors.toMap(
                User::getId,
                Function.identity(),
                (e1, e2) -> e2,
                LinkedHashMap::new
        ));
        Long selectedId = project.getAssignedTo() != null ? project.getAssignedTo().getId() : null;
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
        model.addAttribute("project", project);
        model.addAttribute("values", values);
        model.addAttribute("checkedIndex", selectedIndex);
        return "projects/project_edit_assignedTo";
    }

    @RequestMapping(value = {"/projects/{id}/editAssignedTo"}, method = RequestMethod.POST)
    public String saveTask(Model model, @PathVariable("id") Long id, @RequestParam("radioAssignedTo") Long assignedToId) {
        Project project = projectService.findById(id);

        if (assignedToId != null) {
            User user = userService.findById(assignedToId);
            project.setAssignedTo(user);
        } else {
            project.setAssignedTo(null);
        }
        projectService.save(project);

        return "redirect:/projects";
    }
}
