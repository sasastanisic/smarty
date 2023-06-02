package com.smarty.domain.exam.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record ExamRequestDTO(

        @NotBlank(message = "Exam name can't be blank")
        String name,

        @Min(value = 0, message = "Minimum number of points is 0")
        @Max(value = 30, message = "Maximum number of points is 30")
        double points,

        @NotNull(message = "Date of examination can't be null")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate dateOfExamination,

        String comment,

        @NotNull(message = "Student can't be null")
        Long studentId,

        @NotNull(message = "Course can't be null")
        Long courseId

) {

}
