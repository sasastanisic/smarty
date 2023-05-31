package com.smarty.domain.engagement.service;

import com.smarty.domain.engagement.model.EngagementRequestDTO;
import com.smarty.domain.engagement.model.EngagementResponseDTO;
import com.smarty.domain.engagement.model.EngagementUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EngagementService {

    EngagementResponseDTO createEngagement(EngagementRequestDTO engagementDTO);

    Page<EngagementResponseDTO> getAllEngagements(Pageable pageable);

    EngagementResponseDTO getEngagementById(Long id);

    EngagementResponseDTO updateEngagement(Long id, EngagementUpdateDTO engagementDTO);

    void deleteEngagement(Long id);

}
