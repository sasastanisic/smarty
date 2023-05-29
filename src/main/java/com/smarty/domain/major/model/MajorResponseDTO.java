package com.smarty.domain.major.model;

public record MajorResponseDTO(

        Long id,
        String code,
        String fullName,
        String description,
        int duration

) {

}
