package com.smarty.infrastructure.mapper;

import com.smarty.domain.major.entity.Major;
import com.smarty.domain.major.model.MajorRequestDTO;
import com.smarty.domain.major.model.MajorResponseDTO;
import com.smarty.domain.major.model.MajorUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MajorMapper {

    Major toMajor(MajorRequestDTO majorRequestDTO);

    MajorResponseDTO toMajorResponseDTO(Major major);

    void updateMajorFromDTO(MajorUpdateDTO majorUpdateDTO, @MappingTarget Major major);

}
