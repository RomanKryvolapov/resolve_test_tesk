package com.romankryvolapov.resolve.resolve.controllers;

import com.romankryvolapov.resolve.resolve.models.network.UserDTO;
import com.romankryvolapov.resolve.resolve.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserDTO dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        return userService.update(id, dto);
    }

    @GetMapping
    public List<UserDTO> list() {
        return userService.listAll();
    }
}