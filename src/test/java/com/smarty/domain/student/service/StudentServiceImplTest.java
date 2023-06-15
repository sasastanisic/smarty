package com.smarty.domain.student.service;

import com.smarty.domain.account.entity.Account;
import com.smarty.domain.account.enums.Role;
import com.smarty.domain.account.model.AccountRequestDTO;
import com.smarty.domain.account.model.AccountResponseDTO;
import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.major.entity.Major;
import com.smarty.domain.major.model.MajorResponseDTO;
import com.smarty.domain.major.service.MajorService;
import com.smarty.domain.status.entity.Status;
import com.smarty.domain.status.service.StatusService;
import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.repository.StudentRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.StudentMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    Student student;

    Major major;

    Status status;

    Account account;

    Page<Student> students;

    @InjectMocks
    StudentServiceImpl studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentMapperImpl studentMapper;

    @Mock
    MajorService majorService;

    @Mock
    StatusService statusService;

    @Mock
    AccountService accountService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        major = new Major();
        major.setId(1L);
        major.setCode("SE");
        major.setFullName("Software engineering");
        major.setDescription("Software engineering major");
        major.setDuration(4);

        status = new Status();
        status.setId(1L);
        status.setType("Traditional");

        account = new Account();
        account.setId(1L);
        account.setEmail("sasastanisic4@gmail.com");
        account.setPassword("$password123$");
        account.setRole(Role.STUDENT);

        student = new Student();
        student.setId(1L);
        student.setName("Sasa");
        student.setSurname("Stanisic");
        student.setIndex(4377);
        student.setYear(3);
        student.setSemester(6);
        student.setMajor(major);
        student.setStatus(status);
        student.setAccount(account);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        students = new PageImpl<>(studentList);

        studentService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void testCreateStudent() {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("sasastanisic4@gmail.com", "$password123$", Role.STUDENT);
        StudentRequestDTO studentRequestDTO = new StudentRequestDTO("Sasa", "Stanisic", 4377, 3, 6, 1L, 1L, accountRequestDTO);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("sasastanisic4@gmail.com", "$password123$", Role.STUDENT);
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO(1L, "Sasa", "Stanisic", 4377, 3, 6,
                new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 4),
                new Status(1L, "Traditional"), accountResponseDTO);

        when(studentMapper.toStudent(studentRequestDTO)).thenReturn(student);
        when(majorService.getById(major.getId())).thenReturn(student.getMajor());
        when(statusService.getStatusById(status.getId())).thenReturn(student.getStatus());
        doNothing().when(accountService).existsByEmail(studentRequestDTO.account().email());
        when(passwordEncoder.encode(studentRequestDTO.account().password())).thenReturn("$password123$");
        when(studentRepository.existsByIndex(studentRequestDTO.index())).thenReturn(false);
        when(studentRepository.save(student)).thenReturn(student);
        doReturn(studentResponseDTO).when(studentMapper).toStudentResponseDTO(student);

        var createdStudentDTO = studentService.createStudent(studentRequestDTO);

        assertThat(studentResponseDTO).usingRecursiveComparison().isEqualTo(createdStudentDTO);
    }

    @Test
    void testGetAllStudents() {
        Pageable pageable = mock(Pageable.class);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("sasastanisic4@gmail.com", "$password123$", Role.STUDENT);
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO(1L, "Sasa", "Stanisic", 4377, 3, 6,
                new MajorResponseDTO(1L, "SE", "Software engineering", "Software engineering major", 4),
                new Status(1L, "Traditional"), accountResponseDTO);

        when(studentMapper.toStudentResponseDTO(student)).thenReturn(studentResponseDTO);
        var expectedStudents = students.map(student -> studentMapper.toStudentResponseDTO(student));
        doReturn(students).when(studentRepository).findAll(pageable);
        var studentPage = studentService.getAllStudents(pageable);

        Assertions.assertEquals(expectedStudents, studentPage);
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(studentRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> studentService.deleteStudent(1L));
    }

    @Test
    void testDeleteStudent_NotFound() {
        doReturn(false).when(studentRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> studentService.deleteStudent(1L));
    }

}
