package com.smarty.domain.exam.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.exam.model.ExamRequestDTO;
import com.smarty.domain.exam.model.ExamResponseDTO;
import com.smarty.domain.exam.model.ExamUpdateDTO;
import com.smarty.domain.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamService {

    ExamResponseDTO createExam(ExamRequestDTO examDTO);

    void checkCourseAndStudentYear(Student student, Course course);

    void isExamAlreadyPassed(Student student, Course course);

    Page<ExamResponseDTO> getAllExams(Pageable pageable);

    ExamResponseDTO getExamById(Long id);

    List<ExamResponseDTO> getExamHistoryByStudent(Long studentId);

    List<ExamResponseDTO> getExamHistoryByCourse(Long courseId);

    List<ExamResponseDTO> getPassedExamsByStudent(Long studentId, int year);

    ExamResponseDTO updateExam(Long id, ExamUpdateDTO examDTO);

    void deleteExam(Long id);

}
