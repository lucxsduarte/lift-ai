package com.liftai.backend.entities;

import com.liftai.backend.enums.ExerciseCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "category_code", nullable = false)
    private ExerciseCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;
}