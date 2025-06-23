package com.romankryvolapov.resolve.resolve.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romankryvolapov.resolve.resolve.configs.GlobalExceptionHandler;
import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import com.romankryvolapov.resolve.resolve.models.exceptions.DependencyNotCompletedException;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import com.romankryvolapov.resolve.resolve.models.network.TaskDTO;
import com.romankryvolapov.resolve.resolve.services.TaskService;
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

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        mapper.findAndRegisterModules();
    }

    @Test
    @DisplayName("POST /tasks — creates a task and calls the service")
    void createTask() throws Exception {
        TaskDTO dto = TaskDTO.builder()
                .title("T")
                .description("D")
                .dueDate(LocalDate.now())
                .userId(1L)
                .build();
        TaskDTO saved = TaskDTO.builder()
                .id(5L)
                .title("T")
                .description("D")
                .dueDate(dto.getDueDate())
                .status(TaskStatus.PENDING)
                .userId(1L)
                .build();
        when(taskService.create(any())).thenReturn(saved);

        mvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("T"));

        ArgumentCaptor<TaskDTO> captor = ArgumentCaptor.forClass(TaskDTO.class);
        verify(taskService, times(1)).create(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("T");
    }

    @Test
    @DisplayName("GET /tasks — list all tasks and call check")
    void listTasks() throws Exception {
        TaskDTO t1 = TaskDTO.builder().id(1L).title("A").build();
        TaskDTO t2 = TaskDTO.builder().id(2L).title("B").build();
        when(taskService.listAll()).thenReturn(List.of(t1, t2));

        mvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].title").value("B"));

        verify(taskService, times(1)).listAll();
    }

    @Test
    @DisplayName("GET /tasks/{id} — if id does not exist, returns 404")
    void getByIdNotFound() throws Exception {
        when(taskService.getById(99L))
                .thenThrow(new NotFoundServiceException("Task", 99L));

        mvc.perform(get("/tasks/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task with id=99 not found"));

        verify(taskService, times(1)).getById(99L);
    }

    @Test
    @DisplayName("PUT /tasks/{id} — if the dependency is not completed, returns DependencyNotCompletedException")
    void updateDependencyNotCompleted() throws Exception {
        TaskDTO dto = TaskDTO.builder()
                .title("X")
                .description("Y")
                .dueDate(LocalDate.of(2025, 6, 1))
                .userId(1L)
                .status(TaskStatus.COMPLETED)
                .build();
        when(taskService.update(eq(3L), any()))
                .thenThrow(new DependencyNotCompletedException());

        mvc.perform(put("/tasks/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Cannot complete task with uncompleted dependency"));

        verify(taskService, times(1)).update(eq(3L), any());
    }
}
