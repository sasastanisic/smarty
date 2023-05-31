package com.smarty.domain.engagement.service;

import com.smarty.domain.engagement.repository.EngagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EngagementServiceImpl implements EngagementService {

    private final EngagementRepository engagementRepository;

    @Autowired
    public EngagementServiceImpl(EngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
    }

}
