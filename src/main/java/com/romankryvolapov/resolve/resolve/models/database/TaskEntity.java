package com.romankryvolapov.resolve.resolve.models.database;

import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "app_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "status required")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "depends_on_id")
    private TaskEntity dependsOn;
}