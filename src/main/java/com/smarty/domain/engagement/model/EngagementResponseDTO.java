package com.smarty.domain.engagement.model;

import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;

public record EngagementResponseDTO(

        Long id,
        ProfessorResponseDTO professor,
        CourseResponseDTO course

) {

}
