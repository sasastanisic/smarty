package com.smarty.domain.course.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseRequestDTO;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.model.CourseUpdateDTO;
import com.smarty.domain.course.repository.CourseRepository;
import com.smarty.domain.professor.service.ProfessorService;
import com.smarty.domain.student.service.StudentService;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private static final String COURSE_NOT_EXISTS = "Course with id %d doesn't exist";

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ProfessorService professorService;
    private final StudentService studentService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseMapper courseMapper,
                             @Lazy ProfessorService professorService,
                             @Lazy StudentService studentService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.professorService = professorService;
        this.studentService = studentService;
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
    public void existsById(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NotFoundException(COURSE_NOT_EXISTS.formatted(id));
        }
    }

    @Override
    public void existsByCode(String code) {
        if (!courseRepository.existsByCode(code)) {
            throw new NotFoundException("Course with code %s doesn't exist".formatted(code));
        }
    }

    @Override
    public void existsByYear(int year) {
        int minYear = 1;
        int maxYear = 4;

        if (!courseRepository.existsByYear(year) && (year < minYear || year > maxYear)) {
            throw new NotFoundException("Year %d doesn't exist during the studies".formatted(year));
        }
    }

    private void existsBySemester(int semester) {
        int minSemester = 1;
        int maxSemester = 8;

        if (!courseRepository.existsBySemester(semester) && (semester < minSemester || semester > maxSemester)) {
            throw new NotFoundException("Semester %d doesn't exist during the studies".formatted(semester));
        }
    }

    @Override
    public List<CourseResponseDTO> getCoursesByYear(int year) {
        List<Course> coursesByYear = courseRepository.findByYear(year);
        existsByYear(year);

        if (coursesByYear.isEmpty()) {
            throw new NotFoundException("List of courses by year is empty");
        }

        return getCourseListResponseDTO(coursesByYear);
    }

    @Override
    public List<CourseResponseDTO> getCoursesBySemester(int semester) {
        List<Course> coursesBySemester = courseRepository.findBySemester(semester);
        existsBySemester(semester);

        if (coursesBySemester.isEmpty()) {
            throw new NotFoundException("List of courses by semester is empty");
        }

        return getCourseListResponseDTO(coursesBySemester);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByProfessor(Long professorId) {
        List<Course> coursesByProfessor = courseRepository.findCoursesByProfessor(professorId);
        professorService.existsById(professorId);

        if (coursesByProfessor.isEmpty()) {
            throw new NotFoundException("List of courses by professor is empty");
        }

        return getCourseListResponseDTO(coursesByProfessor);
    }

    @Override
    public List<CourseResponseDTO> getCoursesByStudent(Long studentId) {
        List<Course> coursesByStudent = courseRepository.findCoursesByStudent(studentId);
        studentService.existsById(studentId);

        if (coursesByStudent.isEmpty()) {
            throw new NotFoundException("List of courses by student is empty");
        }

        return getCourseListResponseDTO(coursesByStudent);
    }

    private List<CourseResponseDTO> getCourseListResponseDTO(List<Course> courseList) {
        return courseList
                .stream()
                .map(courseMapper::toCourseResponseDTO)
                .collect(Collectors.toList());
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
