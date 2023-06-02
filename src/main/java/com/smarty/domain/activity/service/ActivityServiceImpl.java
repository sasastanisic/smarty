package com.smarty.domain.activity.service;

import com.smarty.domain.activity.entity.Activity;
import com.smarty.domain.activity.model.ActivityRequestDTO;
import com.smarty.domain.activity.model.ActivityResponseDTO;
import com.smarty.domain.activity.model.ActivityUpdateDTO;
import com.smarty.domain.activity.repository.ActivityRepository;
import com.smarty.domain.course.service.CourseService;
import com.smarty.domain.student.service.StudentService;
import com.smarty.domain.task.enums.Type;
import com.smarty.domain.task.service.TaskService;
import com.smarty.infrastructure.handler.exceptions.ConflictException;
import com.smarty.infrastructure.handler.exceptions.ForbiddenException;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import com.smarty.infrastructure.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    private static final String ACTIVITY_NOT_EXISTS = "Activity with id %d doesn't exist";

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final TaskService taskService;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository,
                               ActivityMapper activityMapper,
                               TaskService taskService,
                               StudentService studentService,
                               CourseService courseService) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
        this.taskService = taskService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Override
    public ActivityResponseDTO createActivity(ActivityRequestDTO activityDTO) {
        Activity activity = activityMapper.toActivity(activityDTO);
        var task = taskService.getById(activityDTO.taskId());
        var student = studentService.getById(activityDTO.studentId());
        var course = courseService.getById(task.getCourse().getId());

        activity.setTask(task);
        activity.setStudent(student);

        validateActivityNameForStudent(activityDTO.activityName(), activityDTO.studentId());
        validateActivityPoints(activityDTO.points(), task.getMaxPoints());
        validateNumberOfActivitiesByTaskType(task.getType(), activityDTO.studentId(), course.getId(), task.getNumberOfTasks());

        activityRepository.save(activity);

        return activityMapper.toActivityResponseDTO(activity);
    }

    private void validateActivityNameForStudent(String activityName, Long studentId) {
        if (activityRepository.existsByActivityNameAndStudent_Id(activityName, studentId)) {
            throw new ConflictException("Activity named %s already exists for student with id %d".formatted(activityName, studentId));
        }
    }

    private void validateActivityPoints(double activityPoints, double maxPoints) {
        if (activityPoints > maxPoints) {
            throw new ForbiddenException("It is not allowed to enter %.2f points for this activity".formatted(activityPoints));
        }
    }

    private void validateNumberOfActivitiesByTaskType(Type type, Long studentId, Long courseId, int numberOfTasks) {
        int numberOfActivities = activityRepository.findNumberOfActivitiesByTaskType(type, studentId, courseId);

        if (numberOfActivities == numberOfTasks) {
            throw new ForbiddenException("Limit for storing activities by type %s is reached".formatted(type));
        }
    }

    @Override
    public Page<ActivityResponseDTO> getAllActivities(Pageable pageable) {
        return activityRepository.findAll(pageable).map(activityMapper::toActivityResponseDTO);
    }

    @Override
    public ActivityResponseDTO getActivityById(Long id) {
        return activityMapper.toActivityResponseDTO(getById(id));
    }

    private Activity getById(Long id) {
        return activityRepository.findById(id).orElseThrow(() -> new NotFoundException(ACTIVITY_NOT_EXISTS.formatted(id)));
    }

    @Override
    public List<ActivityResponseDTO> getStudentActivitiesByCourse(Long studentId, String code) {
        List<Activity> studentActivitiesByCourse = activityRepository.findStudentActivitiesByCourse(studentId, code);
        studentService.existsById(studentId);
        courseService.existsByCode(code);

        if (studentActivitiesByCourse.isEmpty()) {
            throw new NotFoundException("There are 0 activities by course %s for student with id %d".formatted(code, studentId));
        }

        return studentActivitiesByCourse
                .stream()
                .map(activityMapper::toActivityResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityResponseDTO updateActivity(Long id, ActivityUpdateDTO activityDTO) {
        Activity activity = getById(id);
        activityMapper.updateActivityFromDTO(activityDTO, activity);
        var task = taskService.getById(activity.getTask().getId());

        activity.setTask(task);
        validateActivityPoints(activityDTO.points(), task.getMaxPoints());
        activityRepository.save(activity);

        return activityMapper.toActivityResponseDTO(activity);
    }

    @Override
    public void deleteActivity(Long id) {
        if (!activityRepository.existsById(id)) {
            throw new NotFoundException(ACTIVITY_NOT_EXISTS.formatted(id));
        }

        activityRepository.deleteById(id);
    }

}
