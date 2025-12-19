package com.jitda.domain.grade.repository;

import com.jitda.domain.grade.entity.Grade;
import com.jitda.domain.grade.entity.GradeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByName(GradeName name);
}
