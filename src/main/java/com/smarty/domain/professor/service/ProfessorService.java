package com.smarty.domain.professor.service;

import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.model.PasswordDTO;
import com.smarty.domain.professor.model.ProfessorRequestDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;
import com.smarty.domain.professor.model.ProfessorUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfessorService {

    ProfessorResponseDTO createProfessor(ProfessorRequestDTO professorDTO);

    Page<ProfessorResponseDTO> getAllProfessors(Pageable pageable);

    ProfessorResponseDTO getProfessorById(Long id);

    Professor getById(Long id);

    void existsById(Long id);

    List<ProfessorResponseDTO> getProfessorsByCourse(Long courseId);

    ProfessorResponseDTO updateProfessor(Long id, ProfessorUpdateDTO professorDTO);

    ProfessorResponseDTO updatePassword(Long id, PasswordDTO passwordDTO);

    void deleteProfessor(Long id);

}
