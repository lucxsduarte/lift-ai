package com.liftai.backend.controllers;

import com.liftai.backend.entities.Workout;
import com.liftai.backend.entities.WorkoutExercise;
import com.liftai.backend.services.WorkoutService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<Workout> createWorkout(@RequestHeader("User-Id") final UUID userId, @Valid @RequestBody final CreateWorkoutRequest request) {
        final var newWorkout = workoutService.createWorkout(userId, request.name());
        return ResponseEntity.ok(newWorkout);
    }

    @PostMapping("/{workoutId}/exercises")
    public ResponseEntity<WorkoutExercise> addExerciseToWorkout(@PathVariable final UUID workoutId, @Valid @RequestBody final AddExerciseRequest request) {

        final var addedExercise = workoutService.addExerciseToWorkout(
                workoutId,
                request.exerciseId(),
                request.targetSets(),
                request.targetReps(),
                request.baseWeight()
        );
        return ResponseEntity.ok(addedExercise);
    }

    public record CreateWorkoutRequest(
            @NotBlank(message = "O nome do treino não pode ser vazio.")
            String name) {
    }

    public record AddExerciseRequest(
            @NotNull(message = "O ID do exercício é obrigatório.")
            UUID exerciseId,

            @NotNull(message = "A quantidade de séries é obrigatória.")
            @Min(value = 1, message = "O exercício deve ter pelo menos 1 série planejada.")
            Integer targetSets,

            @NotNull(message = "A quantidade de repetições é obrigatória.")
            @Min(value = 1, message = "O exercício deve ter pelo menos 1 repetição planejada.")
            Integer targetReps,

            @NotNull(message = "O peso base é obrigatório.")
            @Min(value = 0, message = "O peso planejado não pode ser negativo.")
            BigDecimal baseWeight
    ) {
    }
}