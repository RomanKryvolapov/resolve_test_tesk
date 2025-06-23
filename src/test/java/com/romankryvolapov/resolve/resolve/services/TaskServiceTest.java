package com.romankryvolapov.resolve.resolve.services;

import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import com.romankryvolapov.resolve.resolve.models.database.TaskEntity;
import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import com.romankryvolapov.resolve.resolve.models.network.TaskDTO;
import com.romankryvolapov.resolve.resolve.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepo;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private final UserEntity user = UserEntity.builder()
            .id(1L)
            .name("Alice")
            .email("alice@example.com")
            .build();

    @Test
    void create_ShouldThrow_WhenDependsOnNotFound() {
        when(userService.findEntity(1L)).thenReturn(user);
        when(taskRepo.findById(99L)).thenReturn(Optional.empty());

        TaskDTO dto = new TaskDTO(
                null, "T", "D", LocalDate.now(), TaskStatus.PENDING, 1L, 99L
        );

        assertThrows(RuntimeException.class, () -> taskService.create(dto));
    }

    @Test
    void create_ShouldSaveAndReturnDtoWithId() {
        when(userService.findEntity(1L)).thenReturn(user);
        TaskDTO dto = new TaskDTO(
                null, "T", "D", LocalDate.of(2025, 6, 23), TaskStatus.PENDING, 1L, null
        );
        TaskEntity saved = TaskEntity.builder()
                .id(5L)
                .title("T")
                .description("D")
                .dueDate(LocalDate.of(2025, 6, 23))
                .status(TaskStatus.PENDING)
                .assignee(user)
                .dependsOn(null)
                .build();
        when(taskRepo.save(any(TaskEntity.class))).thenReturn(saved);

        TaskDTO result = taskService.create(dto);

        assertEquals(5L, result.getId());
        assertEquals("T", result.getTitle());
        assertEquals(1L, result.getUserId());
        verify(taskRepo).save(any(TaskEntity.class));
    }

    @Test
    void update_ShouldThrow_WhenCompletingDependentNotDone() {
        TaskEntity parent = TaskEntity.builder()
                .id(2L).status(TaskStatus.PENDING)
                .assignee(user).build();
        TaskEntity child = TaskEntity.builder()
                .id(3L).status(TaskStatus.IN_PROGRESS)
                .assignee(user).dependsOn(parent).build();
        when(taskRepo.findById(3L)).thenReturn(Optional.of(child));

        TaskDTO dto = new TaskDTO(null, null, null, null, TaskStatus.COMPLETED, null, null);
        assertThrows(RuntimeException.class, () -> taskService.update(3L, dto));
    }

    @Test
    void update_ShouldChangeStatus_WhenNoDependency() {
        TaskEntity t = TaskEntity.builder()
                .id(4L)
                .status(TaskStatus.IN_PROGRESS)
                .assignee(user)
                .dependsOn(null)
                .build();
        when(taskRepo.findById(4L)).thenReturn(Optional.of(t));
        when(taskRepo.save(t)).thenReturn(t);

        TaskDTO dto = new TaskDTO(null, null, null, null, TaskStatus.COMPLETED, null, null);
        TaskDTO result = taskService.update(4L, dto);

        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepo).save(t);
    }

    @Test
    void listAll_ShouldReturnAllTasks() {
        TaskEntity t1 = TaskEntity.builder().id(1L).title("A").assignee(user).build();
        TaskEntity t2 = TaskEntity.builder().id(2L).title("B").assignee(user).build();
        when(taskRepo.findAll()).thenReturn(Arrays.asList(t1, t2));

        var list = taskService.listAll();

        assertEquals(2, list.size());
        assertEquals("A", list.get(0).getTitle());
        assertEquals("B", list.get(1).getTitle());
    }

    @Test
    void listByUser_ShouldReturnTasksForUser() {
        TaskEntity t = TaskEntity.builder().id(7L).title("X").assignee(user).build();
        when(userService.findEntity(1L)).thenReturn(user);
        when(taskRepo.findByAssignee(user)).thenReturn(Arrays.asList(t));

        var list = taskService.listByUser(1L);

        assertEquals(1, list.size());
        assertEquals(7L, list.get(0).getId());
    }
}