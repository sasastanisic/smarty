package com.smarty.domain.engagement.service;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.engagement.entity.Engagement;
import com.smarty.domain.engagement.model.EngagementRequestDTO;
import com.smarty.domain.engagement.model.EngagementResponseDTO;
import com.smarty.domain.engagement.model.EngagementUpdateDTO;
import com.smarty.domain.engagement.repository.EngagementRepository;
import com.smarty.domain.professor.entity.Professor;
import com.smarty.domain.professor.service.ProfessorService;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.EngagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EngagementServiceImpl implements EngagementService {

    private static final String ENGAGEMENT_NOT_EXISTS = "Engagement with id %d doesn't exist";

    private final EngagementRepository engagementRepository;
    private final EngagementMapper engagementMapper;
    private final ProfessorService professorService;
    private final CourseService courseService;

    @Autowired
    public EngagementServiceImpl(EngagementRepository engagementRepository,
                                 EngagementMapper engagementMapper,
                                 ProfessorService professorService,
                                 CourseService courseService) {
        this.engagementRepository = engagementRepository;
        this.engagementMapper = engagementMapper;
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @Override
    public EngagementResponseDTO createEngagement(EngagementRequestDTO engagementDTO) {
        Engagement engagement = engagementMapper.toEngagement(engagementDTO);
        var professor = professorService.getById(engagementDTO.professorId());
        var course = courseService.getById(engagementDTO.courseId());

        return getEngagementResponseDTO(engagement, professor, course);
    }

    private EngagementResponseDTO getEngagementResponseDTO(Engagement engagement, Professor professor, Course course) {
        engagement.setProfessor(professor);
        engagement.setCourse(course);
        validateExistsByProfessorAndCourse(professor, course);
        engagementRepository.save(engagement);

        return engagementMapper.toEngagementResponseDTO(engagement);
    }

    private void validateExistsByProfessorAndCourse(Professor professor, Course course) {
        if (engagementRepository.existsByProfessorAndCourse(professor, course)) {
            throw new ConflictException("Engagement already exists for professor %s and course %s".formatted(professor.getName(), course.getCode()));
        }
    }

    @Override
    public Page<EngagementResponseDTO> getAllEngagements(Pageable pageable) {
        return engagementRepository.findAll(pageable).map(engagementMapper::toEngagementResponseDTO);
    }

    @Override
    public EngagementResponseDTO getEngagementById(Long id) {
        return engagementMapper.toEngagementResponseDTO(getById(id));
    }

    private Engagement getById(Long id) {
        return engagementRepository.findById(id).orElseThrow(() -> new NotFoundException(ENGAGEMENT_NOT_EXISTS.formatted(id)));
    }

    @Override
    public EngagementResponseDTO updateEngagement(Long id, EngagementUpdateDTO engagementDTO) {
        Engagement engagement = getById(id);
        engagementMapper.updateEngagementFromDTO(engagementDTO, engagement);
        var professor = professorService.getById(engagementDTO.professorId());
        var course = courseService.getById(engagementDTO.courseId());

        return getEngagementResponseDTO(engagement, professor, course);
    }

    @Override
    public void deleteEngagement(Long id) {
        if (!engagementRepository.existsById(id)) {
            throw new NotFoundException(ENGAGEMENT_NOT_EXISTS.formatted(id));
        }

        engagementRepository.deleteById(id);
    }

}
