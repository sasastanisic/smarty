package com.smarty.domain.task.model;

import com.smarty.domain.task.enums.Type;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDTO(

        @NotNull(message = "Type of task can't be null")
        Type type,

        @Min(value = 1, message = "Minimum value for points is 1")
        @Max(value = 30, message = "Maximum value for points is 30")
        double maxPoints,

        @Min(value = 1, message = "Minimum number of tasks is 1")
        @Max(value = 15, message = "Maximum number of tasks is 15")
        int numberOfTasks,

        @NotNull(message = "Course can't be null")
        Long courseId

) {

}
