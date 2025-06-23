package com.romankryvolapov.resolve.resolve.repository;

import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import com.romankryvolapov.resolve.resolve.models.database.TaskEntity;
import com.romankryvolapov.resolve.resolve.models.database.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    void saveAndFindByAssignee() {
        UserEntity u = userRepo.save(UserEntity.builder()
                .name("U")
                .email("u@e")
                .build());

        TaskEntity t1 = TaskEntity.builder()
                .title("T1")
                .description("")
                .dueDate(LocalDate.now())
                .status(TaskStatus.PENDING)
                .assignee(u)
                .build();
        taskRepo.save(t1);

        List<TaskEntity> list = taskRepo.findByAssignee(u);
        assertEquals(1, list.size());
        assertEquals("T1", list.get(0).getTitle());
    }

    @Test
    void findAll() {
        UserEntity u = userRepo.save(UserEntity.builder().name("X").email("x").build());
        taskRepo.save(TaskEntity.builder()
                .title("A").description("").dueDate(LocalDate.now())
                .status(TaskStatus.IN_PROGRESS).assignee(u).build());
        taskRepo.save(TaskEntity.builder()
                .title("B").description("").dueDate(LocalDate.now())
                .status(TaskStatus.PENDING).assignee(u).build());

        var all = taskRepo.findAll();
        assertEquals(2, all.size());
    }
}
