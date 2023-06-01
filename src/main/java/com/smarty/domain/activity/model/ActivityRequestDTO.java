package com.smarty.domain.activity.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActivityRequestDTO(

        @NotBlank(message = "Activity name can't be blank")
        String activityName,

        @Min(value = 0, message = "Number of points can't be negative")
        double points,

        String comment,

        @NotNull(message = "Task can't be null")
        Long taskId,

        @NotNull(message = "Student can't be null")
        Long studentId

) {

}
