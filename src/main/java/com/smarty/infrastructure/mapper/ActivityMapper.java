package com.smarty.infrastructure.mapper;

import com.smarty.domain.activity.entity.Activity;
import com.smarty.domain.activity.model.ActivityRequestDTO;
import com.smarty.domain.activity.model.ActivityResponseDTO;
import com.smarty.domain.activity.model.ActivityUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {

    Activity toActivity(ActivityRequestDTO activityRequestDTO);

    ActivityResponseDTO toActivityResponseDTO(Activity activity);

    void updateActivityFromDTO(ActivityUpdateDTO activityUpdateDTO, @MappingTarget Activity activity);

}
