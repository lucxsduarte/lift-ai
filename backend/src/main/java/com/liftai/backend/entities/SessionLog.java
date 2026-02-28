package com.liftai.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "session_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private WorkoutSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "sets_completed", nullable = false)
    private Integer setsCompleted;

    @Column(name = "reps_completed", nullable = false)
    private Integer repsCompleted;

    @Column(name = "weight_used", nullable = false)
    private BigDecimal weightUsed;

    @Column(columnDefinition = "TEXT")
    private String notes;
}