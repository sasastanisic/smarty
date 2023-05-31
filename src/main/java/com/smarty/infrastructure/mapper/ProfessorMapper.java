package com.smarty.infrastructure.mapper;

import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.model.ProfessorRequestDTO;
import com.smarty.domain.professor.model.ProfessorResponseDTO;
import com.smarty.domain.professor.model.ProfessorUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    Professor toProfessor(ProfessorRequestDTO professorRequestDTO);

    ProfessorResponseDTO toProfessorResponseDTO(Professor professor);

    void updateProfessorFromDTO(ProfessorUpdateDTO professorUpdateDTO, @MappingTarget Professor professor);

}
