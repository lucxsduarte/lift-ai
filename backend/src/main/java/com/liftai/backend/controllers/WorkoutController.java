package com.liftai.backend.controllers;

import com.liftai.backend.entities.Workout;
import com.liftai.backend.entities.WorkoutExercise;
import com.liftai.backend.services.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<Workout>> getUserWorkouts(@RequestHeader("User-Id") UUID userId) {
        return ResponseEntity.ok(workoutService.findWorkoutsByUser(userId));
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(@RequestHeader("User-Id") final UUID userId,  @RequestBody final CreateWorkoutRequest request) {
        final var newWorkout = workoutService.createWorkout(userId, request.name());
        return ResponseEntity.ok(newWorkout);
    }

    @PostMapping("/{workoutId}/exercises")
    public ResponseEntity<WorkoutExercise> addExerciseToWorkout( @PathVariable UUID workoutId, @RequestBody AddExerciseRequest request) {

        final var addedExercise = workoutService.addExerciseToWorkout(
                workoutId,
                request.exerciseId(),
                request.targetSets(),
                request.targetReps(),
                request.baseWeight()
        );
        return ResponseEntity.ok(addedExercise);
    }

    public record CreateWorkoutRequest(String name) {}

    public record AddExerciseRequest(
            UUID exerciseId,
            Integer targetSets,
            Integer targetReps,
            BigDecimal baseWeight
    ) {}
}