package com.smarty.domain.exam.repository;

import com.smarty.domain.course.entity.Course;
import com.smarty.domain.exam.entity.Exam;
import com.smarty.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("SELECT COUNT(e) > 0 FROM exam e " +
            "JOIN student s ON e.student.id = s.id " +
            "JOIN course c ON e.course.id = c.id " +
            "WHERE s = :student AND c = :course AND e.grade > 5")
    boolean isExamAlreadyPassed(Student student, Course course);

}
