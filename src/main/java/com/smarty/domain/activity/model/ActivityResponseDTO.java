package com.smarty.domain.activity.model;

import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.task.model.TaskResponseDTO;

public record ActivityResponseDTO(

        Long id,
        String activityName,
        double points,
        String comment,
        TaskResponseDTO task,
        StudentResponseDTO student

) {

}
