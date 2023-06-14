package com.smarty.domain.professor.repository;

import com.smarty.domain.professor.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Professor findByAccount_Email(String email);

    @Query("SELECT p FROM professor p " +
            "JOIN engagement e ON p.id = e.professor.id " +
            "JOIN course c ON e.course.id = c.id " +
            "WHERE c.id = :courseId")
    List<Professor> findProfessorsByCourse(Long courseId);

}
