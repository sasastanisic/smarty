package com.smarty.domain.exam.model;

import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.student.model.StudentResponseDTO;

import java.time.LocalDate;

public record ExamResponseDTO(

        Long id,
        String name,
        int grade,
        double points,
        LocalDate dateOfExamination,
        String comment,
        double totalPoints,
        StudentResponseDTO student,
        CourseResponseDTO course

) {

}
