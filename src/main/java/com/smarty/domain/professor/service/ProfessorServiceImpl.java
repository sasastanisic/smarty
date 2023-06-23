package com.smarty.domain.professor.service;

import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.model.PasswordDTO;
import com.smarty.domain.professor.model.ProfessorRequestDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;
import com.smarty.domain.professor.model.ProfessorUpdateDTO;
import com.smarty.domain.professor.repository.ProfessorRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ProfessorMapper;
import com.smarty.infrastructure.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private static final String PROFESSOR_NOT_EXISTS = "Professor with id %d doesn't exist";

    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;
    private final AccountService accountService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ProfessorServiceImpl(ProfessorRepository professorRepository,
                                ProfessorMapper professorMapper,
                                AccountService accountService,
                                @Lazy CourseService courseService,
                                AuthenticationService authenticationService) {
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
        this.accountService = accountService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
    }

    @Override
    public ProfessorResponseDTO createProfessor(ProfessorRequestDTO professorDTO) {
        Professor professor = professorMapper.toProfessor(professorDTO);
        accountService.existsByEmail(professorDTO.account().email());

        var encryptedPassword = encodePassword(professorDTO.account().password());

        professor.getAccount().setPassword(encryptedPassword);
        professorRepository.save(professor);

        return professorMapper.toProfessorResponseDTO(professor);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public Page<ProfessorResponseDTO> getAllProfessors(Pageable pageable) {
        return professorRepository.findAll(pageable).map(professorMapper::toProfessorResponseDTO);
    }

    @Override
    public ProfessorResponseDTO getProfessorById(Long id) {
        return professorMapper.toProfessorResponseDTO(getById(id));
    }

    public Professor getById(Long id) {
        Optional<Professor> optionalProfessor = professorRepository.findById(id);

        if (optionalProfessor.isEmpty()) {
            throw new NotFoundException(PROFESSOR_NOT_EXISTS.formatted(id));
        }

        return optionalProfessor.get();
    }

    @Override
    public ProfessorResponseDTO getProfessorByEmail(String email) {
        Professor professor = professorRepository.findByAccount_Email(email);

        if (professor == null) {
            throw new NotFoundException("Professor with email %s doesn't exist".formatted(email));
        }

        return professorMapper.toProfessorResponseDTO(professor);
    }

    @Override
    public void existsById(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new NotFoundException(PROFESSOR_NOT_EXISTS.formatted(id));
        }
    }

    @Override
    public List<ProfessorResponseDTO> getProfessorsByCourse(Long courseId) {
        List<Professor> professorsByCourse = professorRepository.findProfessorsByCourse(courseId);
        courseService.existsById(courseId);

        if (professorsByCourse.isEmpty()) {
            throw new NotFoundException("List of professors by course is empty");
        }

        return professorsByCourse
                .stream()
                .map(professorMapper::toProfessorResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProfessorResponseDTO updateProfessor(Long id, ProfessorUpdateDTO professorDTO) {
        Professor professor = getById(id);
        professorMapper.updateProfessorFromDTO(professorDTO, professor);

        var encryptedPassword = encodePassword(professorDTO.account().password());

        professor.getAccount().setPassword(encryptedPassword);
        professorRepository.save(professor);

        return professorMapper.toProfessorResponseDTO(professor);
    }

    @Override
    public ProfessorResponseDTO updatePassword(Long id, PasswordDTO passwordDTO) {
        Professor professor = getById(id);

        authenticationService.canUpdatePassword(professor.getAccount().getEmail());
        arePasswordsMatching(passwordDTO.password(), passwordDTO.confirmedPassword());
        var encryptedPassword = encodePassword(passwordDTO.password());

        professor.getAccount().setPassword(encryptedPassword);
        professorRepository.save(professor);

        return professorMapper.toProfessorResponseDTO(professor);
    }

    private void arePasswordsMatching(String password, String confirmedPassword) {
        if (!password.matches(confirmedPassword)) {
            throw new NotFoundException("Passwords aren't matching");
        }
    }

    @Override
    public void deleteProfessor(Long id) {
        existsById(id);

        professorRepository.deleteById(id);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
