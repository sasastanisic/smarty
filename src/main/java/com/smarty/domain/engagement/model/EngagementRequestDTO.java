package com.smarty.domain.engagement.model;

import jakarta.validation.constraints.NotNull;

public record EngagementRequestDTO(

        @NotNull(message = "Professor can't be null")
        Long professorId,

        @NotNull(message = "Course can't be null")
        Long courseId

) {

}
