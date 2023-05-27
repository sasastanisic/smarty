package com.smarty.domain.student.model;

public record StudentResponseDTO(

        Long id,
        String name,
        String surname,
        int index,
        int year,
        int semester,
        double averageGrade

) {

}
