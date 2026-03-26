package com.smartrecurit.backend.repository;

import com.smartrecurit.backend.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// ApplicationRepository.java
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByJobId(Long jobId);
    List<Application> findByCandidateId(Long candidateId);
    Optional<Application> findByCandidateIdAndJobId(Long candidateId, Long jobId);
}
