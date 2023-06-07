package com.smarty.domain.student.service;

import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.major.service.MajorService;
import com.smarty.domain.status.service.StatusService;
import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.model.PasswordDTO;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.model.StudentUpdateDTO;
import com.smarty.domain.student.repository.StudentRepository;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.StudentMapper;
import com.smarty.infrastructure.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private static final String STUDENT_NOT_EXISTS = "Student with id %d doesn't exist";

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final MajorService majorService;
    private final StatusService statusService;
    private final AccountService accountService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              StudentMapper studentMapper,
                              MajorService majorService,
                              StatusService statusService,
                              AccountService accountService,
                              CourseService courseService,
                              AuthenticationService authenticationService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.majorService = majorService;
        this.statusService = statusService;
        this.accountService = accountService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
    }

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO studentDTO) {
        Student student = studentMapper.toStudent(studentDTO);
        var major = majorService.getById(studentDTO.majorId());
        var status = statusService.getStatusById(studentDTO.statusId());
        accountService.existsByEmail(studentDTO.account().email());

        var encryptedPassword = encodePassword(studentDTO.account().password());

        student.setMajor(major);
        student.setStatus(status);
        student.getAccount().setPassword(encryptedPassword);
        validateIndex(studentDTO.index());
        studentRepository.save(student);

        return studentMapper.toStudentResponseDTO(student);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
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

    public Student getById(Long id) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            throw new NotFoundException(STUDENT_NOT_EXISTS.formatted(id));
        }

        return optionalStudent.get();
    }

    @Override
    public void existsById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException(STUDENT_NOT_EXISTS.formatted(id));
        }
    }

    @Override
    public List<StudentResponseDTO> getStudentsByMajor(Long majorId) {
        List<Student> studentsByMajor = studentRepository.findStudentsByMajor_Id(majorId);
        majorService.existsById(majorId);

        if (studentsByMajor.isEmpty()) {
            throw new NotFoundException("List of students by major is empty");
        }

        return getStudentListResponseDTO(studentsByMajor);
    }

    @Override
    public List<StudentResponseDTO> getStudentsByStudyStatus(Long statusId) {
        List<Student> studentsByStudyStatus = studentRepository.findStudentsByStatus_Id(statusId);
        statusService.existsById(statusId);

        if (studentsByStudyStatus.isEmpty()) {
            throw new NotFoundException("List of students by study status is empty");
        }

        return getStudentListResponseDTO(studentsByStudyStatus);
    }

    @Override
    public List<StudentResponseDTO> getStudentsWhoPassedCertainCourse(Long courseId) {
        List<Student> studentsWhoPassedCertainCourse = studentRepository.findStudentsWhoPassedCertainCourse(courseId);
        courseService.existsById(courseId);

        if (studentsWhoPassedCertainCourse.isEmpty()) {
            throw new NotFoundException("There are 0 students that passed course with id %d".formatted(courseId));
        }

        return getStudentListResponseDTO(studentsWhoPassedCertainCourse);
    }

    private List<StudentResponseDTO> getStudentListResponseDTO(List<Student> studentList) {
        return studentList
                .stream()
                .map(studentMapper::toStudentResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentUpdateDTO studentDTO) {
        Student student = getById(id);
        var major = majorService.getById(studentDTO.majorId());
        var status = statusService.getStatusById(studentDTO.statusId());
        studentMapper.updateStudentFromDTO(studentDTO, student);

        var encryptedPassword = encodePassword(studentDTO.account().password());

        student.setMajor(major);
        student.setStatus(status);
        student.getAccount().setPassword(encryptedPassword);
        studentRepository.save(student);

        return studentMapper.toStudentResponseDTO(student);
    }

    @Override
    public StudentResponseDTO updatePassword(Long id, PasswordDTO passwordDTO) {
        Student student = getById(id);

        authenticationService.canUpdatePassword(student.getAccount().getEmail());
        arePasswordsMatching(passwordDTO.password(), passwordDTO.confirmedPassword());
        var encryptedPassword = encodePassword(passwordDTO.password());

        student.getAccount().setPassword(encryptedPassword);
        studentRepository.save(student);

        return studentMapper.toStudentResponseDTO(student);
    }

    private void arePasswordsMatching(String password, String confirmedPassword) {
        if (!password.matches(confirmedPassword)) {
            throw new NotFoundException("Passwords aren't matching");
        }
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException(STUDENT_NOT_EXISTS.formatted(id));
        }

        studentRepository.deleteById(id);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
