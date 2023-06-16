package com.smarty.domain.course.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseRequestDTO;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.model.CourseUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    CourseResponseDTO createCourse(CourseRequestDTO courseDTO);

    Page<CourseResponseDTO> getAllCourses(Pageable pageable);

    CourseResponseDTO getCourseById(Long id);

    Course getById(Long id);

    void existsById(Long id);

    void existsByCode(String code);

    void existsByYear(int year);

    List<CourseResponseDTO> getCoursesByYear(int year);

    List<CourseResponseDTO> getCoursesBySemester(int semester);

    List<CourseResponseDTO> getCoursesByProfessor(Long professorId);

    List<CourseResponseDTO> getCoursesByStudent(Long studentId);

    CourseResponseDTO updateCourse(Long id, CourseUpdateDTO courseDTO);

    void deleteCourse(Long id);

}
