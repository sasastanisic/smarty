package com.smarty.domain.student.repository;

import com.smarty.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByIndex(int index);

    List<Student> findStudentsByMajor_Id(Long majorId);

    List<Student> findStudentsByStatus_Id(Long statusId);

}
