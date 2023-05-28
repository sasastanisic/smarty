package com.smarty.domain.course.model;

public record CourseResponseDTO(

        Long id,
        String code,
        String fullName,
        double points,
        int year,
        int semester,
        String description

) {

}
