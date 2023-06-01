package com.smarty.domain.task.service;

import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.model.TaskRequestDTO;
import com.smarty.domain.task.model.TaskResponseDTO;
import com.smarty.domain.task.model.TaskUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO taskDTO);

    Page<TaskResponseDTO> getAllTasks(Pageable pageable);

    TaskResponseDTO getTaskById(Long id);

    Task getById(Long id);

    TaskResponseDTO updateTask(Long id, TaskUpdateDTO taskDTO);

    void deleteTask(Long id);

}
