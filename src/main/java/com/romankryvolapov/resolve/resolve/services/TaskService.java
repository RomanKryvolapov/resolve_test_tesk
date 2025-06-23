package com.romankryvolapov.resolve.resolve.services;

import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import com.romankryvolapov.resolve.resolve.models.database.TaskEntity;
import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import com.romankryvolapov.resolve.resolve.models.exceptions.AppException;
import com.romankryvolapov.resolve.resolve.models.exceptions.DependencyNotCompletedException;
import com.romankryvolapov.resolve.resolve.models.exceptions.NotFoundServiceException;
import com.romankryvolapov.resolve.resolve.models.network.TaskDTO;
import com.romankryvolapov.resolve.resolve.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserService userService;

    @Transactional
    public TaskDTO create(TaskDTO dto) {
        UserEntity user = userService.findEntity(dto.getUserId());
        TaskEntity parent = null;
        if (dto.getDependsOnId() != null) {
            parent = taskRepo.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new NotFoundServiceException("Task", dto.getDependsOnId()));
        }

        TaskStatus initialStatus = dto.getStatus() != null
                ? dto.getStatus()
                : TaskStatus.PENDING;

        if (initialStatus == TaskStatus.COMPLETED
                && parent != null
                && parent.getStatus() != TaskStatus.COMPLETED) {
            throw new DependencyNotCompletedException();
        }

        TaskEntity t = TaskEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .dueDate(dto.getDueDate())
                .status(initialStatus)
                .assignee(user)
                .dependsOn(parent)
                .build();
        TaskEntity saved = taskRepo.save(t);
        return toDto(saved);
    }

    public List<TaskDTO> listAll() {
        return taskRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public TaskDTO getById(Long id) {
        return taskRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundServiceException("Task", id));
    }

    @Transactional
    public TaskDTO update(Long id, TaskDTO dto) {
        TaskEntity t = taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundServiceException("Task", id));

        if (dto.getStatus() == TaskStatus.COMPLETED && t.getDependsOn() != null
                && t.getDependsOn().getStatus() != TaskStatus.COMPLETED) {
            throw new DependencyNotCompletedException();
        }

        if (dto.getTitle() != null) {
            t.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            t.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            t.setDueDate(dto.getDueDate());
        }
        if (dto.getStatus() != null) {
            t.setStatus(dto.getStatus());
        }
        if (dto.getUserId() != null) {
            UserEntity u = userService.findEntity(dto.getUserId());
            t.setAssignee(u);
        }

        if (dto.getDependsOnId() != null) {
            TaskEntity parent = taskRepo.findById(dto.getDependsOnId())
                    .orElseThrow(() -> new NotFoundServiceException("Task", dto.getDependsOnId()));
            if (hasCycle(t, parent)) {
                throw new AppException("Cyclic dependency detected");
            }
            t.setDependsOn(parent);
        } else if (dto.getDependsOnId() == null) {
            t.setDependsOn(null);
        }

        TaskEntity updated = taskRepo.save(t);
        return toDto(updated);
    }

    public List<TaskDTO> listByUser(Long userId) {
        UserEntity u = userService.findEntity(userId);
        return taskRepo.findByAssignee(u).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private TaskDTO toDto(TaskEntity t) {
        return TaskDTO.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .dueDate(t.getDueDate())
                .status(t.getStatus())
                .userId(t.getAssignee().getId())
                .dependsOnId(t.getDependsOn() != null ? t.getDependsOn().getId() : null)
                .build();
    }

    private boolean hasCycle(TaskEntity start, TaskEntity candidate) {
        TaskEntity current = candidate.getDependsOn();
        while (current != null) {
            if (current.getId().equals(start.getId())) {
                return true;
            }
            current = current.getDependsOn();
        }
        return false;
    }
}