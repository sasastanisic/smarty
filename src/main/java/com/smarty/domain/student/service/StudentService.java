package com.smarty.domain.student.service;

import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.model.PasswordDTO;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.model.StudentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO studentDTO);

    Page<StudentResponseDTO> getAllStudents(Pageable pageable);

    StudentResponseDTO getStudentById(Long id);

    Student getById(Long id);

    StudentResponseDTO getStudentByEmail(String email);

    void existsById(Long id);

    List<StudentResponseDTO> getStudentsByMajor(Long majorId);

    List<StudentResponseDTO> getStudentsByStudyStatus(Long statusId);

    List<StudentResponseDTO> getStudentsWhoPassedCertainCourse(Long courseId);

    StudentResponseDTO updateStudent(Long id, StudentUpdateDTO studentDTO);

    StudentResponseDTO updatePassword(Long id, PasswordDTO passwordDTO);

    void deleteStudent(Long id);

}
