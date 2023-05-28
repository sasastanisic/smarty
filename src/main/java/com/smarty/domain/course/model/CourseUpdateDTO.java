package com.smarty.domain.course.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CourseUpdateDTO(

        @NotBlank(message = "Full name of course can't be blank")
        String fullName,

        @Min(value = 4, message = "Minimum value for points of course is 4")
        @Max(value = 10, message = "Maximum value for points of course is 10")
        double points,

        @Min(value = 1, message = "Minimum value for year is 1")
        @Max(value = 4, message = "Maximum value for year is 4")
        int year,

        @Min(value = 1, message = "Value of semester is minimum 1")
        @Max(value = 8, message = "Value of semester is maximum 8")
        int semester,

        @NotBlank(message = "Course description can't be blank")
        String description

) {

}
