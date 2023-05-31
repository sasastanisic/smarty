package com.smarty.domain.engagement.repository;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.engagement.entity.Engagement;
import com.smarty.domain.professor.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngagementRepository extends JpaRepository<Engagement, Long> {

    boolean existsByProfessorAndCourse(Professor professor, Course course);

}
