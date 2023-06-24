package com.smarty.domain.professor.service;

import com.smarty.domain.account.entity.Account;
import com.smarty.domain.account.enums.Role;
import com.smarty.domain.account.model.AccountRequestDTO;
import com.smarty.domain.account.model.AccountResponseDTO;
import com.smarty.domain.account.model.AccountUpdateDTO;
import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.model.PasswordDTO;
import com.smarty.domain.professor.model.ProfessorRequestDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;
import com.smarty.domain.professor.model.ProfessorUpdateDTO;
import com.smarty.domain.professor.repository.ProfessorRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ProfessorMapperImpl;
import com.smarty.infrastructure.security.AuthenticationService;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceImplTest {

    Professor professor;

    Account account;

    Page<Professor> professors;

    @InjectMocks
    ProfessorServiceImpl professorService;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    ProfessorMapperImpl professorMapper;

    @Mock
    AccountService accountService;

    @Mock
    CourseService courseService;

    @Mock
    AuthenticationService authenticationService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setEmail("bojansimovic34@gmail.com");
        account.setPassword("bojan34");
        account.setRole(Role.PROFESSOR);

        professor = new Professor();
        professor.setId(1L);
        professor.setName("Bojan");
        professor.setSurname("Simovic");
        professor.setYearsOfExperience(5);
        professor.setAccount(account);

        List<Professor> professorList = new ArrayList<>();
        professorList.add(professor);
        professors = new PageImpl<>(professorList);

        professorService.setPasswordEncoder(passwordEncoder);
    }

    @Test
    void testCreateProfessor() {
        AccountRequestDTO accountRequestDTO = new AccountRequestDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorRequestDTO professorRequestDTO = new ProfessorRequestDTO("Bojan", "Simovic", 5, accountRequestDTO);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorMapper.toProfessor(professorRequestDTO)).thenReturn(professor);
        doNothing().when(accountService).existsByEmail(professorRequestDTO.account().email());
        when(passwordEncoder.encode(professorRequestDTO.account().password())).thenReturn(accountResponseDTO.password());
        when(professorRepository.save(professor)).thenReturn(professor);
        doReturn(professorResponseDTO).when(professorMapper).toProfessorResponseDTO(professor);

        var createdProfessorDTO = professorService.createProfessor(professorRequestDTO);

        assertThat(professorResponseDTO).usingRecursiveComparison().isEqualTo(createdProfessorDTO);
    }

    @Test
    void testGetAllProfessors() {
        Pageable pageable = mock(Pageable.class);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorMapper.toProfessorResponseDTO(professor)).thenReturn(professorResponseDTO);
        var expectedProfessors = professors.map(professor -> professorMapper.toProfessorResponseDTO(professor));
        doReturn(professors).when(professorRepository).findAll(pageable);
        var professorPage = professorService.getAllProfessors(pageable);

        assertThat(expectedProfessors).usingRecursiveComparison().isEqualTo(professorPage);
    }

    @Test
    void testGetProfessorById() {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorMapper.toProfessorResponseDTO(professor)).thenReturn(professorResponseDTO);
        var expectedProfessor = professorMapper.toProfessorResponseDTO(professor);
        doReturn(Optional.of(professor)).when(professorRepository).findById(1L);
        var returnedProfessor = professorService.getProfessorById(1L);

        assertThat(expectedProfessor).usingRecursiveComparison().isEqualTo(returnedProfessor);
    }

    @Test
    void testGetProfessorById_NotFound() {
        doReturn(Optional.empty()).when(professorRepository).findById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> professorService.getProfessorById(1L));
    }

    @Test
    void testGetProfessorByEmail() {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorMapper.toProfessorResponseDTO(professor)).thenReturn(professorResponseDTO);
        var expectedProfessor = professorMapper.toProfessorResponseDTO(professor);
        doReturn(professor).when(professorRepository).findByAccount_Email(professor.getAccount().getEmail());
        var returnedProfessor = professorService.getProfessorByEmail(professor.getAccount().getEmail());

        assertThat(expectedProfessor).usingRecursiveComparison().isEqualTo(returnedProfessor);
    }

    @Test
    void testProfessorExists() {
        doReturn(true).when(professorRepository).existsById(1L);
        Assertions.assertDoesNotThrow(() -> professorService.existsById(1L));
        verify(professorRepository, times(1)).existsById(1L);
    }

    @Test
    void testProfessorExists_NotFound() {
        doReturn(false).when(professorRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> professorService.existsById(1L));
    }

    @Test
    void testGetProfessorsByCourse() {
        List<Professor> professorsByCourse = List.of(professor);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "bojan34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorMapper.toProfessorResponseDTO(professor)).thenReturn(professorResponseDTO);
        var expectedList = professorsByCourse
                .stream()
                .map(professorMapper::toProfessorResponseDTO)
                .toList();
        doReturn(professorsByCourse).when(professorRepository).findProfessorsByCourse(new Course().getId());
        doNothing().when(courseService).existsById(new Course().getId());
        var returnedList = professorService.getProfessorsByCourse(new Course().getId());

        Assertions.assertTrue(professorsByCourse.contains(professor));
        Assertions.assertEquals(expectedList, returnedList);
        Assertions.assertFalse(returnedList.isEmpty());
    }

    @Test
    void testUpdateProfessor() {
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO("boki34", Role.PROFESSOR);
        ProfessorUpdateDTO professorUpdateDTO = new ProfessorUpdateDTO("Bojan", "Simovic", 5, accountUpdateDTO);

        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "boki34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        doCallRealMethod().when(professorMapper).updateProfessorFromDTO(professorUpdateDTO, professor);
        when(passwordEncoder.encode(professorUpdateDTO.account().password())).thenReturn(accountResponseDTO.password());
        when(professorRepository.save(professor)).thenReturn(professor);
        doReturn(professorResponseDTO).when(professorMapper).toProfessorResponseDTO(professor);

        var updatedProfessorDTO = professorService.updateProfessor(1L, professorUpdateDTO);

        assertThat(professorResponseDTO).usingRecursiveComparison().isEqualTo(updatedProfessorDTO);
    }

    @Test
    void testUpdatePassword() {
        PasswordDTO passwordDTO = new PasswordDTO("boki34", "boki34");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO("bojansimovic34@gmail.com", "boki34", Role.PROFESSOR);
        ProfessorResponseDTO professorResponseDTO = new ProfessorResponseDTO(1L, "Bojan", "Simovic", 5, accountResponseDTO);

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        doNothing().when(authenticationService).canUpdatePassword(professor.getAccount().getEmail());
        when(passwordEncoder.encode(passwordDTO.password())).thenReturn(accountResponseDTO.password());
        when(professorRepository.save(professor)).thenReturn(professor);
        doReturn(professorResponseDTO).when(professorMapper).toProfessorResponseDTO(professor);

        var updatedProfessorDTO = professorService.updatePassword(1L, passwordDTO);

        assertThat(professorResponseDTO).usingRecursiveComparison().isEqualTo(updatedProfessorDTO);
    }

    @Test
    void testDeleteProfessor() {
        when(professorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(professorRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(() -> professorService.deleteProfessor(1L));
    }

    @Test
    void testDeleteProfessor_NotFound() {
        doReturn(false).when(professorRepository).existsById(1L);
        Assertions.assertThrows(NotFoundException.class, () -> professorService.deleteProfessor(1L));
    }

}
