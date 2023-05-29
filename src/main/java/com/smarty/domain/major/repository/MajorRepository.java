package com.smarty.domain.major.repository;

import com.smarty.domain.major.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {

    boolean existsByCode(String code);

}
