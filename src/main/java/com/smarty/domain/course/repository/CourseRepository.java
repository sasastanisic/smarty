package com.smarty.domain.course.repository;

import com.smarty.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    boolean existsByYear(int year);

    boolean existsBySemester(int semester);

    List<Course> findByYear(int year);

    List<Course> findBySemester(int semester);

    @Query("SELECT c FROM course c " +
            "JOIN engagement e ON c.id = e.course.id " +
            "JOIN professor p ON e.professor.id = p.id " +
            "WHERE p.id = :professorId")
    List<Course> findCoursesByProfessor(Long professorId);

}
