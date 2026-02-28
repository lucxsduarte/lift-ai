package com.liftai.backend.repositories;

import com.liftai.backend.entities.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findByWorkoutIdOrderByOrderIndexAsc(UUID workoutId);
}