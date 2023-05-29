package com.smarty.domain.student.service;

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

    List<StudentResponseDTO> getStudentsByMajor(Long majorId);

    List<StudentResponseDTO> getStudentsByStudyStatus(Long statusId);

    StudentResponseDTO updateStudent(Long id, StudentUpdateDTO studentDTO);

    void deleteStudent(Long id);

}
