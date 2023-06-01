package com.smarty.domain.task.service;

import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.model.TaskRequestDTO;
import com.smarty.domain.task.model.TaskResponseDTO;
import com.smarty.domain.task.model.TaskUpdateDTO;
import com.smarty.domain.task.repository.TaskRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NOT_EXISTS = "Task with id %d doesn't exist";

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CourseService courseService;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository,
                           TaskMapper taskMapper,
                           CourseService courseService) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.courseService = courseService;
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskDTO) {
        Task task = taskMapper.toTask(taskDTO);
        var course = courseService.getById(taskDTO.courseId());

        task.setCourse(course);
        taskRepository.save(task);

        return taskMapper.toTaskResponseDTO(task);
    }

    @Override
    public Page<TaskResponseDTO> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable).map(taskMapper::toTaskResponseDTO);
    }

    @Override
    public TaskResponseDTO getTaskById(Long id) {
        return taskMapper.toTaskResponseDTO(getById(id));
    }

    public Task getById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isEmpty()) {
            throw new NotFoundException(TASK_NOT_EXISTS.formatted(id));
        }

        return optionalTask.get();
    }

    @Override
    public TaskResponseDTO updateTask(Long id, TaskUpdateDTO taskDTO) {
        Task task = getById(id);
        taskMapper.updateTaskFromDTO(taskDTO, task);

        taskRepository.save(task);

        return taskMapper.toTaskResponseDTO(task);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException(TASK_NOT_EXISTS.formatted(id));
        }

        taskRepository.deleteById(id);
    }

}
