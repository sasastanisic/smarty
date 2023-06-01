package com.smarty.domain.task.model;

import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.task.enums.Type;

public record TaskResponseDTO(

        Long id,
        Type type,
        double maxPoints,
        int numberOfTasks,
        CourseResponseDTO course

) {

}
