// UserControllerTest
package com.romankryvolapov.resolve.resolve.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romankryvolapov.resolve.resolve.configs.GlobalExceptionHandler;
import com.romankryvolapov.resolve.resolve.models.exceptions.AppException;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import com.romankryvolapov.resolve.resolve.models.network.UserDTO;
import com.romankryvolapov.resolve.resolve.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /users — creates a user and checks the call")
    void createUser() throws Exception {
        UserDTO dto = new UserDTO(null, "Alice", "a@e.com");
        UserDTO saved = new UserDTO(1L, "Alice", "a@e.com");
        when(userService.create(any())).thenReturn(saved);

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));

        ArgumentCaptor<UserDTO> captor = ArgumentCaptor.forClass(UserDTO.class);
        verify(userService, times(1)).create(captor.capture());
        assertThat(captor.getValue().getEmail()).isEqualTo("a@e.com");
    }

    @Test
    @DisplayName("GET /users — list all users and call check")
    void listUsers() throws Exception {
        UserDTO u1 = new UserDTO(1L, "A", "a@e");
        UserDTO u2 = new UserDTO(2L, "B", "b@e");
        when(userService.listAll()).thenReturn(List.of(u1, u2));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("A"))
                .andExpect(jsonPath("$[1].name").value("B"));

        verify(userService, times(1)).listAll();
    }

    @Test
    @DisplayName("PUT /users/{id} — if id does not exist, returns 404")
    void updateUserNotFound() throws Exception {
        UserDTO dto = new UserDTO(null, "Bob", "bob@e.com");
        when(userService.update(eq(42L), any(UserDTO.class)))
                .thenThrow(new NotFoundServiceException("User", 42L));

        mvc.perform(put("/users/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User with id=42 not found"));

        verify(userService, times(1)).update(eq(42L), any(UserDTO.class));
    }

    @Test
    @DisplayName("POST /users — for any AppException returns error 400")
    void createUserAppException() throws Exception {
        UserDTO dto = new UserDTO(null, "X", "x@e.com");
        when(userService.create(any()))
                .thenThrow(new AppException("custom error"));

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("custom error"));

        verify(userService, times(1)).create(any());
    }
}
