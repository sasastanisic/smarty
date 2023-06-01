package com.smarty.domain.task.repository;

import com.smarty.domain.task.entity.Task;
import com.smarty.domain.task.enums.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByTypeAndCourse_Id(Type type, Long courseId);

    List<Task> findByCourse_Id(Long courseId);

    @Query("SELECT SUM(t.maxPoints * t.numberOfTasks) FROM task t " +
            "WHERE t.course.id = :courseId")
    Double findTotalTaskPointsByCourse(Long courseId);

}
