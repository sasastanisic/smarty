package com.smarty.infrastructure.mapper;

import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.model.TaskRequestDTO;
import com.smarty.domain.task.model.TaskResponseDTO;
import com.smarty.domain.task.model.TaskUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toTask(TaskRequestDTO taskRequestDTO);

    TaskResponseDTO toTaskResponseDTO(Task task);

    void updateTaskFromDTO(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);

}
