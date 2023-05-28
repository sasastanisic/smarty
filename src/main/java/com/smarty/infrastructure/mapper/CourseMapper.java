package com.smarty.infrastructure.mapper;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.course.model.CourseRequestDTO;
import com.smarty.domain.course.model.CourseResponseDTO;
import com.smarty.domain.course.model.CourseUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toCourse(CourseRequestDTO courseRequestDTO);

    CourseResponseDTO toCourseResponseDTO(Course course);

    void updateCourseFromDTO(CourseUpdateDTO courseUpdateDTO, @MappingTarget Course course);

}
