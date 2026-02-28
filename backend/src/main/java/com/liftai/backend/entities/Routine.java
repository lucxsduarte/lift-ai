package com.liftai.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "routines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_dynamic_mode", nullable = false)
    private boolean isDynamicMode;

    @Column(name = "paused_routine_id")
    private UUID pausedRoutineId;
}