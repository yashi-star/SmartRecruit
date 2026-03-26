package com.smartrecurit.backend.repository;

import com.smartrecurit.backend.entity.Decision;
import com.smartrecurit.backend.enums.DecisionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DecisionRepository extends JpaRepository<Decision, Long> {
    Optional<Decision> findByScheduleId(Long scheduleId);
    List<Decision> findByDecision(DecisionType decision);
}