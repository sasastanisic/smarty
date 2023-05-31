package com.smarty.domain.course.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseRequestDTO;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.model.CourseUpdateDTO;
import com.smarty.domain.course.repository.CourseRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String COURSE_NOT_EXISTS = "Course with id %d doesn't exist";

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO courseDTO) {
        Course course = courseMapper.toCourse(courseDTO);

        validateCode(courseDTO.code());
        courseRepository.save(course);

        return courseMapper.toCourseResponseDTO(course);
    }

    private void validateCode(String code) {
        if (courseRepository.existsByCode(code)) {
            throw new ConflictException("Course with code %s already exists".formatted(code));
        }
    }

    @Override
    public Page<CourseResponseDTO> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(courseMapper::toCourseResponseDTO);
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        return courseMapper.toCourseResponseDTO(getById(id));
    }

    public Course getById(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isEmpty()) {
            throw new NotFoundException(COURSE_NOT_EXISTS.formatted(id));
        }

        return optionalCourse.get();
    }

    @Override
    public CourseResponseDTO updateCourse(Long id, CourseUpdateDTO courseDTO) {
        Course course = getById(id);
        courseMapper.updateCourseFromDTO(courseDTO, course);

        courseRepository.save(course);

        return courseMapper.toCourseResponseDTO(course);
    }

    @Override
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException(COURSE_NOT_EXISTS.formatted(id));
        }

        courseRepository.deleteById(id);
    }

}
