package com.smarty.domain.professor.service;

import com.smarty.domain.account.service.AccountService;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.model.ProfessorRequestDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;
import com.smarty.domain.professor.model.ProfessorUpdateDTO;
import com.smarty.domain.professor.repository.ProfessorRepository;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ProfessorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public ProfessorServiceImpl(ProfessorRepository professorRepository,
                                ProfessorMapper professorMapper,
                                AccountService accountService,
                                @Lazy CourseService courseService) {
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
        this.accountService = accountService;
        this.courseService = courseService;
    }

    @Override
    public ProfessorResponseDTO createProfessor(ProfessorRequestDTO professorDTO) {
        Professor professor = professorMapper.toProfessor(professorDTO);
        accountService.existsByEmail(professorDTO.account().email());

        professorRepository.save(professor);

        return professorMapper.toProfessorResponseDTO(professor);
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

        professorRepository.save(professor);

        return professorMapper.toProfessorResponseDTO(professor);
    }

    @Override
    public void deleteProfessor(Long id) {
        if (!professorRepository.existsById(id)) {
            throw new NotFoundException(PROFESSOR_NOT_EXISTS.formatted(id));
        }

        professorRepository.deleteById(id);
    }

}
