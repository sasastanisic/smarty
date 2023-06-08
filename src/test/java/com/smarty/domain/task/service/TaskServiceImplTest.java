package com.smarty.domain.task.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.enums.Type;
import com.smarty.domain.task.model.TaskRequestDTO;
import com.smarty.domain.task.model.TaskResponseDTO;
import com.smarty.domain.task.model.TaskUpdateDTO;
import com.smarty.domain.task.repository.TaskRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
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
import java.util.Optional;

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
        when(courseService.getById(course.getId())).thenReturn(task.getCourse());
        when(taskRepository.existsByTypeAndCourse_Id(taskRequestDTO.type(), taskRequestDTO.courseId())).thenReturn(false);
        when(taskRepository.findTotalTaskPointsByCourse(taskRequestDTO.courseId())).thenReturn(47.5);
        when(taskRepository.save(task)).thenReturn(task);
        doReturn(taskResponseDTO).when(taskMapper).toTaskResponseDTO(task);

        var createdTaskDTO = taskService.createTask(taskRequestDTO);

        assertThat(taskResponseDTO).isEqualTo(createdTaskDTO);
        Assertions.assertEquals(taskResponseDTO, createdTaskDTO);
    }

    @Test
    void testTaskByTypeAndCourseId() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(Type.HOMEWORK, 1.5, 15, 1L);

        when(taskMapper.toTask(taskRequestDTO)).thenReturn(task);
        doReturn(false).when(taskRepository).existsByTypeAndCourse_Id(Type.HOMEWORK, taskRequestDTO.courseId());

        Assertions.assertDoesNotThrow(() -> taskService.createTask(taskRequestDTO));
    }

    @Test
    void testTaskByTypeAndCourseId_NotValid() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO(Type.HOMEWORK, 1.5, 15, 1L);

        when(taskMapper.toTask(taskRequestDTO)).thenReturn(task);
        doReturn(true).when(taskRepository).existsByTypeAndCourse_Id(Type.HOMEWORK, taskRequestDTO.courseId());

        Assertions.assertThrows(ConflictException.class, () -> taskService.createTask(taskRequestDTO));
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
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(1L, Type.HOMEWORK, 1.5, 15, courseResponseDTO);

        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);
        var expectedTask = taskMapper.toTaskResponseDTO(task);
        doReturn(Optional.of(task)).when(taskRepository).findById(1L);
        var returnedTask = taskService.getTaskById(1L);

        Assertions.assertEquals(expectedTask, returnedTask);
    }

    @Test
    void testGetTaskById_NotFound() {
        doReturn(Optional.empty()).when(taskRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testGetTasksByCourse() {
        List<Task> tasksByCourse = List.of(task);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(1L, Type.HOMEWORK, 1.5, 15, courseResponseDTO);

        when(taskMapper.toTaskResponseDTO(task)).thenReturn(taskResponseDTO);
        var expectedList = tasksByCourse
                .stream()
                .map(taskMapper::toTaskResponseDTO)
                .toList();
        doReturn(tasksByCourse).when(taskRepository).findByCourse_Id(course.getId());
        var returnedList = taskService.getTasksByCourse(course.getId());

        Assertions.assertTrue(tasksByCourse.contains(task));
        Assertions.assertEquals(expectedList, returnedList);
        Assertions.assertFalse(returnedList.isEmpty());
    }

    @Test
    void testUpdateTask() {
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO(2, 12);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO(1L, Type.HOMEWORK, 2, 12, courseResponseDTO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doCallRealMethod().when(taskMapper).updateTaskFromDTO(taskUpdateDTO, task);
        when(taskRepository.save(task)).thenReturn(task);
        doReturn(taskResponseDTO).when(taskMapper).toTaskResponseDTO(task);

        var updatedTaskDTO = taskService.updateTask(1L, taskUpdateDTO);

        assertThat(taskResponseDTO).isEqualTo(updatedTaskDTO);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> taskService.deleteTask(1L));
    }

    @Test
    void testDeleteTask_NotFound() {
        doReturn(false).when(taskRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> taskService.deleteTask(1L));
    }

}
