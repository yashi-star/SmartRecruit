package com.smartrecurit.backend.repository;

import com.smartrecurit.backend.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    List<InterviewSchedule> findByInterviewerId(Long interviewerId);
    List<InterviewSchedule> findByCandidateId(Long candidateId);

    // This query checks for overlapping slots — your conflict detection!
    @Query("""
        SELECT COUNT(s) FROM InterviewSchedule s
        WHERE (s.interviewer.id = :userId OR s.candidate.id = :userId)
        AND s.scheduledAt < :endTime
        AND FUNCTION('TIMESTAMPADD', MINUTE, s.durationMinutes, s.scheduledAt) > :startTime
    """)
    long countOverlapping(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
