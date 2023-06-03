package com.smarty.domain.task.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.enums.Type;
import com.smarty.domain.task.model.TaskRequestDTO;
import com.smarty.domain.task.model.TaskResponseDTO;
import com.smarty.domain.task.repository.TaskRepository;
import com.smarty.infrastructure.mapper.TaskMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    Course course;

    Task task;

    Page<Task> tasks;

    @InjectMocks
    TaskServiceImpl taskService;

    @Mock
    TaskRepository taskRepository;

    @Mock
    TaskMapperImpl taskMapper;

    @Mock
    CourseService courseService;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setId(1L);
        course.setCode("IT355");
        course.setFullName("Web Systems 2");
        course.setPoints(8);
        course.setYear(3);
        course.setSemester(6);
        course.setDescription("Course about learning backend framework Spring and Spring Boot");

        task = new Task();
        task.setId(1L);
        task.setType(Type.HOMEWORK);
        task.setMaxPoints(1.5);
        task.setNumberOfTasks(15);
        task.setCourse(course);

        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        tasks = new PageImpl<>(taskList);
    }

    @Test
    void testCreateTask() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(Type.HOMEWORK, 1.5, 15, 1L);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(1L, Type.HOMEWORK, 1.5, 15, courseResponseDTO);

        when(taskMapper.toTask(taskRequestDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

        var createdTaskDTO = taskService.createTask(taskRequestDTO);

        assertThat(taskResponseDTO).isEqualTo(createdTaskDTO);
        Assertions.assertEquals(taskResponseDTO, createdTaskDTO);
    }

    @Test
    void testGetAllTasks() {
        Pageable pageable = mock(Pageable.class);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(1L, Type.HOMEWORK, 1.5, 15, courseResponseDTO);
        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);

        var expectedTasks = tasks.map(task -> taskMapper.toTaskResponseDTO(task));
        doReturn(tasks).when(taskRepository).findAll(pageable);
        var taskPage = taskService.getAllTasks(pageable);

        Assertions.assertEquals(expectedTasks, taskPage);
    }

    @Test
    void testGetTaskById() {

    }

    @Test
    void testUpdateTask() {

    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> taskService.deleteTask(1L));
    }

}
