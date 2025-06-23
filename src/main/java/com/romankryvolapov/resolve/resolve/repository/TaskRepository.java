package com.romankryvolapov.resolve.resolve.repository;

import com.romankryvolapov.resolve.resolve.models.database.TaskEntity;
import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByAssignee(UserEntity user);
}