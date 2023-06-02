package com.smarty.domain.activity.service;

import com.smarty.domain.activity.model.ActivityRequestDTO;
import com.smarty.domain.activity.model.ActivityResponseDTO;
import com.smarty.domain.activity.model.ActivityUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService {

    ActivityResponseDTO createActivity(ActivityRequestDTO activityDTO);

    Page<ActivityResponseDTO> getAllActivities(Pageable pageable);

    ActivityResponseDTO getActivityById(Long id);

    List<ActivityResponseDTO> getStudentActivitiesByCourse(Long studentId, String code);

    ActivityResponseDTO updateActivity(Long id, ActivityUpdateDTO activityDTO);

    void deleteActivity(Long id);

}
