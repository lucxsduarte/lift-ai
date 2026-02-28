package com.liftai.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "routine_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoutineDay {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;
}