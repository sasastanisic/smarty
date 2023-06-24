package com.smarty.domain.course.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseRequestDTO;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.model.CourseUpdateDTO;
import com.smarty.domain.course.repository.CourseRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.CourseMapperImpl;
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
public class CourseServiceImplTest {

    Course course;

    Page<Course> courses;

    @InjectMocks
    CourseServiceImpl courseService;

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseMapperImpl courseMapper;

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

        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        courses = new PageImpl<>(courseList);
    }

    @Test
    void testCreateCourse() {
        CourseRequestDTO courseRequestDTO = new CourseRequestDTO("IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");

        when(courseMapper.toCourse(courseRequestDTO)).thenReturn(course);
        when(courseRepository.existsByCode(courseRequestDTO.code())).thenReturn(false);
        when(courseRepository.save(course)).thenReturn(course);
        doReturn(courseResponseDTO).when(courseMapper).toCourseResponseDTO(course);

        var createdCourseDTO = courseService.createCourse(courseRequestDTO);

        assertThat(courseResponseDTO).isEqualTo(createdCourseDTO);
    }

    @Test
    void testCourseByCode_NotValid() {
        CourseRequestDTO courseRequestDTO = new CourseRequestDTO("IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");

        when(courseMapper.toCourse(courseRequestDTO)).thenReturn(course);
        doReturn(true).when(courseRepository).existsByCode("IT355");

        Assertions.assertThrows(ConflictException.class, () -> courseService.createCourse(courseRequestDTO));
    }

    @Test
    void testGetAllCourses() {
        Pageable pageable = mock(Pageable.class);
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");

        when(courseMapper.toCourseResponseDTO(course)).thenReturn(courseResponseDTO);
        var expectedCourses = courses.map(course -> courseMapper.toCourseResponseDTO(course));
        doReturn(courses).when(courseRepository).findAll(pageable);
        var coursePage = courseService.getAllCourses(pageable);

        Assertions.assertEquals(expectedCourses, coursePage);
    }

    @Test
    void testGetCourseById() {
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 8, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");

        when(courseMapper.toCourseResponseDTO(course)).thenReturn(courseResponseDTO);
        var expectedCourse = courseMapper.toCourseResponseDTO(course);
        doReturn(Optional.of(course)).when(courseRepository).findById(1L);
        var returnedCourse = courseService.getCourseById(1L);

        Assertions.assertEquals(expectedCourse, returnedCourse);
    }

    @Test
    void testGetCourseById_NotFound() {
        doReturn(Optional.empty()).when(courseRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> courseService.getCourseById(1L));
    }

    @Test
    void testUpdateCourse() {
        CourseUpdateDTO courseUpdateDTO = new CourseUpdateDTO("Web Systems 2", 10, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO(1L, "IT355", "Web Systems 2", 10, 3, 6,
                "Course about learning backend framework Spring and Spring Boot");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doCallRealMethod().when(courseMapper).updateCourseFromDTO(courseUpdateDTO, course);
        when(courseRepository.save(course)).thenReturn(course);
        doReturn(courseResponseDTO).when(courseMapper).toCourseResponseDTO(course);

        var updatedCourseDTO = courseService.updateCourse(1L, courseUpdateDTO);

        assertThat(courseResponseDTO).isEqualTo(updatedCourseDTO);
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> courseService.deleteCourse(1L));
    }

    @Test
    void testDeleteCourse_NotFound() {
        doReturn(false).when(courseRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> courseService.deleteCourse(1L));
    }

}
