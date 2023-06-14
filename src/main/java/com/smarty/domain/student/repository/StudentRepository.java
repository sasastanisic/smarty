package com.smarty.domain.student.repository;

import com.smarty.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByIndex(int index);

    Student findByAccount_Email(String email);

    List<Student> findStudentsByMajor_Id(Long majorId);

    List<Student> findStudentsByStatus_Id(Long statusId);

    @Query("SELECT s FROM student s " +
            "JOIN exam e ON s.id = e.student.id " +
            "WHERE e.grade > 5 AND e.course.id = :courseId")
    List<Student> findStudentsWhoPassedCertainCourse(Long courseId);

}
