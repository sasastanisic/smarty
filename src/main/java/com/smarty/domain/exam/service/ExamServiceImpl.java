package com.smarty.domain.exam.service;

import com.smarty.domain.activity.service.ActivityService;
import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.exam.entity.Exam;
import com.smarty.domain.exam.model.ExamRequestDTO;
import com.smarty.domain.exam.model.ExamResponseDTO;
import com.smarty.domain.exam.model.ExamUpdateDTO;
import com.smarty.domain.exam.repository.ExamRepository;
import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.service.StudentService;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.ForbiddenException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {

    private static final String EXAM_NOT_EXISTS = "Exam with id %d doesn't exist";
    private static final int MIN_ACTIVITY_POINTS_REQUIRED = 35;

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ActivityService activityService;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository,
                           ExamMapper examMapper,
                           StudentService studentService,
                           CourseService courseService,
                           @Lazy ActivityService activityService) {
        this.examRepository = examRepository;
        this.examMapper = examMapper;
        this.studentService = studentService;
        this.courseService = courseService;
        this.activityService = activityService;
    }

    @Override
    public ExamResponseDTO createExam(ExamRequestDTO examDTO) {
        Exam exam = examMapper.toExam(examDTO);
        var student = studentService.getById(examDTO.studentId());
        var course = courseService.getById(examDTO.courseId());

        double totalPoints = getTotalPoints(examDTO.studentId(), examDTO.courseId(), examDTO.points());
        int grade = calculateGrade(totalPoints);
        grade = checkGrade(examDTO.points(), grade);

        exam.setGrade(grade);
        exam.setTotalPoints(totalPoints);
        exam.setStudent(student);
        exam.setCourse(course);

        checkCourseAndStudentYear(student, course);
        isExamAlreadyPassed(student, course);
        validateTotalActivityPoints(examDTO.studentId(), examDTO.courseId());

        examRepository.save(exam);

        return examMapper.toExamResponseDTO(exam);
    }

    private double getTotalPoints(Long studentId, Long courseId, double examPoints) {
        var totalPoints = activityService.getTotalActivityPointsByCourse(studentId, courseId);

        if (totalPoints == null) {
            totalPoints = 0.0;
        }

        return totalPoints + examPoints;
    }

    private int calculateGrade(double totalPoints) {
        int bestGrade = 10;

        return switch ((int) Math.floor(totalPoints / 10)) {
            case 10, 9 -> bestGrade;
            case 8 -> 9;
            case 7 -> 8;
            case 6 -> 7;
            case 5 -> 6;
            default -> 5;
        };
    }

    private int checkGrade(double examPoints, int grade) {
        if (examPoints < 15) {
            grade = 5;
        }

        return grade;
    }

    @Override
    public void checkCourseAndStudentYear(Student student, Course course) {
        if (student.getYear() < course.getYear()) {
            throw new ForbiddenException("Student %s can't take the exam because course %s is in a year higher than the student's year of study"
                    .formatted(student.getName(), course.getCode()));
        }
    }

    @Override
    public void isExamAlreadyPassed(Student student, Course course) {
        if (examRepository.isExamAlreadyPassed(student, course)) {
            throw new ConflictException("Student %s has already passed the %s exam".formatted(student.getName(), course.getCode()));
        }
    }

    private void validateTotalActivityPoints(Long studentId, Long courseId) {
        var totalActivityPoints = activityService.getTotalActivityPointsByCourse(studentId, courseId);

        if (totalActivityPoints == null) {
            totalActivityPoints = 0.0;
        }

        if (totalActivityPoints < MIN_ACTIVITY_POINTS_REQUIRED) {
            throw new ForbiddenException("Student can't take the exam because he needs at least 35 points for activities. " +
                    "Right now he has %.2f points".formatted(totalActivityPoints));
        }
    }

    @Override
    public Page<ExamResponseDTO> getAllExams(Pageable pageable) {
        return examRepository.findAll(pageable).map(examMapper::toExamResponseDTO);
    }

    @Override
    public ExamResponseDTO getExamById(Long id) {
        return examMapper.toExamResponseDTO(getById(id));
    }

    private Exam getById(Long id) {
        return examRepository.findById(id).orElseThrow(() -> new NotFoundException(EXAM_NOT_EXISTS.formatted(id)));
    }

    @Override
    public List<ExamResponseDTO> getExamHistoryByStudent(Long studentId) {
        List<Exam> examHistoryByStudent = examRepository.findExamHistoryByStudent(studentId);
        studentService.existsById(studentId);

        if (examHistoryByStudent.isEmpty()) {
            throw new NotFoundException("Exam history by student is empty");
        }

        return getExamListResponseDTO(examHistoryByStudent);
    }

    @Override
    public List<ExamResponseDTO> getPassedExamsByStudent(Long studentId, int year) {
        List<Exam> passedExamsByStudent = examRepository.findPassedExamsByStudent(studentId, year);
        studentService.existsById(studentId);
        courseService.existsByYear(year);

        if (passedExamsByStudent.isEmpty()) {
            throw new NotFoundException("List of passed exams is empty");
        }

        return getExamListResponseDTO(passedExamsByStudent);
    }

    private List<ExamResponseDTO> getExamListResponseDTO(List<Exam> examList) {
        return examList
                .stream()
                .map(examMapper::toExamResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExamResponseDTO updateExam(Long id, ExamUpdateDTO examDTO) {
        Exam exam = getById(id);
        examMapper.updateExamFromDTO(examDTO, exam);

        validateTotalActivityPoints(exam.getStudent().getId(), exam.getCourse().getId());
        examRepository.save(exam);

        return examMapper.toExamResponseDTO(exam);
    }

    @Override
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new NotFoundException(EXAM_NOT_EXISTS.formatted(id));
        }

        examRepository.deleteById(id);
    }

}
