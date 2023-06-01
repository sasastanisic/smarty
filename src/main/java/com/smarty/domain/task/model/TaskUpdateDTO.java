package com.smarty.domain.task.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TaskUpdateDTO(

        @Min(value = 1, message = "Minimum value for points is 1")
        @Max(value = 30, message = "Maximum value for points is 30")
        double maxPoints,

        @Min(value = 1, message = "Minimum number of tasks is 1")
        @Max(value = 15, message = "Maximum number of tasks is 15")
        int numberOfTasks

) {

}
