package com.romankryvolapov.resolve.resolve.models.network;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romankryvolapov.resolve.resolve.models.TaskStatus;
import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private Long id;

    @NotBlank(message = "title should not be blank")
    private String title;

    @NotBlank(message = "description should not be blank")
    private String description;

    @NotNull(message = "dueDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private TaskStatus status;

    @NotNull(message = "userId is required")
    private Long userId;

    private Long dependsOnId;
}