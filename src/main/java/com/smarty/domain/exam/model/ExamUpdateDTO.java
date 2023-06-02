package com.smarty.domain.exam.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ExamUpdateDTO(

        @Min(value = 15, message = "Minimum number of points is 15")
        @Max(value = 30, message = "Maximum number of points is 30")
        double points,

        String comment

) {

}
