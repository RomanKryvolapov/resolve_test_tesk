package com.romankryvolapov.resolve.resolve.controllers;

import com.romankryvolapov.resolve.resolve.models.network.TaskDTO;
import com.romankryvolapov.resolve.resolve.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(
            @Valid
            @RequestBody
            TaskDTO dto
    ) {
        return taskService.create(dto);
    }

    @GetMapping
    public List<TaskDTO> list() {
        return taskService.listAll();
    }

    @GetMapping("/{id}")
    public TaskDTO get(
            @PathVariable Long id
    ) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO update(
            @PathVariable
            Long id,
            @Valid
            @RequestBody
            TaskDTO dto
    ) {
        return taskService.update(id, dto);
    }

    @GetMapping("/user/{userId}")
    public List<TaskDTO> listByUser(@PathVariable Long userId) {
        return taskService.listByUser(userId);
    }

}