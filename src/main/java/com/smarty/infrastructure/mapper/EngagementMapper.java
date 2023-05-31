package com.smarty.infrastructure.mapper;

import com.smarty.domain.engagement.entity.Engagement;
import com.smarty.domain.engagement.model.EngagementRequestDTO;
import com.smarty.domain.engagement.model.EngagementResponseDTO;
import com.smarty.domain.engagement.model.EngagementUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EngagementMapper {

    Engagement toEngagement(EngagementRequestDTO engagementRequestDTO);

    EngagementResponseDTO toEngagementResponseDTO(Engagement engagement);

    void updateEngagementFromDTO(EngagementUpdateDTO engagementUpdateDTO, @MappingTarget Engagement engagement);

}
