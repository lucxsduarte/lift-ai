package com.liftai.backend.repositories;

import com.liftai.backend.entities.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findByWorkoutIdOrderByOrderIndexAsc(UUID workoutId);

    @Modifying
    @Query("DELETE FROM WorkoutExercise we WHERE we.workout.id = :workoutId")
    void deleteByWorkoutId(@Param("workoutId") UUID workoutId);
}