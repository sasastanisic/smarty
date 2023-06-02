package com.smarty.domain.exam.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.exam.model.ExamRequestDTO;
import com.smarty.domain.exam.model.ExamResponseDTO;
import com.smarty.domain.exam.model.ExamUpdateDTO;
import com.smarty.domain.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {

    ExamResponseDTO createExam(ExamRequestDTO examDTO);

    void checkCourseAndStudentYear(Student student, Course course);

    void isExamAlreadyPassed(Student student, Course course);

    Page<ExamResponseDTO> getAllExams(Pageable pageable);

    ExamResponseDTO getExamById(Long id);

    ExamResponseDTO updateExam(Long id, ExamUpdateDTO examDTO);

    void deleteExam(Long id);

}
