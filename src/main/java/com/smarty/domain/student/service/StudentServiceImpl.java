package com.smarty.domain.student.service;

import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.model.StudentUpdateDTO;
import com.smarty.domain.student.repository.StudentRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_EXISTS = "Student with id %d doesn't exist";

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final AccountService accountService;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentMapper studentMapper,
                              AccountService accountService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.accountService = accountService;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO studentDTO) {
        Student student = studentMapper.toStudent(studentDTO);

        accountService.existsByEmail(studentDTO.account().email());
        validateIndex(studentDTO.index());
        studentRepository.save(student);

        return studentMapper.toStudentResponseDTO(student);
    }

    private void validateIndex(int index) {
        if (studentRepository.existsByIndex(index)) {
            throw new ConflictException("Student with index %d already exists".formatted(index));
        }
    }

    @Override
    public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toStudentResponseDTO);
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        return studentMapper.toStudentResponseDTO(getById(id));
    }

    private Student getById(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            throw new NotFoundException(STUDENT_NOT_EXISTS.formatted(id));
        }

        return optionalStudent.get();
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentUpdateDTO studentDTO) {
        Student student = getById(id);
        studentMapper.updateStudentFromDTO(studentDTO, student);

        studentRepository.save(student);

        return studentMapper.toStudentResponseDTO(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException(STUDENT_NOT_EXISTS.formatted(id));
        }

        studentRepository.deleteById(id);
    }

}
