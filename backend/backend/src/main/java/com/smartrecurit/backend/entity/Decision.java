package com.smartrecurit.backend.entity;

import com.smartrecurit.backend.enums.DecisionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "decisions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Decision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private InterviewSchedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DecisionType decision;

    private LocalDateTime decidedAt;

    @PrePersist
    public void prePersist() {
        this.decidedAt = LocalDateTime.now();
    }
}