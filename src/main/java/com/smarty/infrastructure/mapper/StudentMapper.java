package com.smarty.infrastructure.mapper;

import com.smarty.domain.student.entity.Student;
import com.smarty.domain.student.model.StudentRequestDTO;
import com.smarty.domain.student.model.StudentResponseDTO;
import com.smarty.domain.student.model.StudentUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toStudent(StudentRequestDTO studentRequestDTO);

    StudentResponseDTO toStudentResponseDTO(Student student);

    void updateStudentFromDTO(StudentUpdateDTO studentUpdateDTO, @MappingTarget Student student);

}
