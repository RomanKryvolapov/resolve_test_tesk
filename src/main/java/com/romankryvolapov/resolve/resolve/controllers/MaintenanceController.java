package com.romankryvolapov.resolve.resolve.controllers;

import com.romankryvolapov.resolve.resolve.repository.TaskRepository;
import com.romankryvolapov.resolve.resolve.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class MaintenanceController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @DeleteMapping("/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void clearDatabase() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }
}
