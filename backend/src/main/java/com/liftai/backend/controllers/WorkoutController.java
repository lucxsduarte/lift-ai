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

    @GetMapping("/{workoutId}")
    public ResponseEntity<WorkoutResponseDTO> getWorkoutById(
            @RequestHeader("User-Id") final UUID userId,
            @PathVariable final UUID workoutId) {

        final var workout = workoutService.findWorkoutByIdAndUser(workoutId, userId);

        final var exerciseDTOs = workout.getExercises().stream()
                .map(we -> new WorkoutExerciseDTO(
                        we.getId(),
                        we.getExercise().getId(),
                        we.getExercise().getName(),
                        we.getTargetSets(),
                        we.getTargetReps(),
                        we.getBaseWeight(),
                        we.getOrderIndex()
                ))
                .toList();

        final var response = new WorkoutResponseDTO(
                workout.getId(),
                workout.getName(),
                exerciseDTOs
        );

        return ResponseEntity.ok(response);
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

    @PutMapping("/{workoutId}/exercises")
    public ResponseEntity<Void> updateWorkoutExercises(
            @RequestHeader("User-Id") final UUID userId,
            @PathVariable final UUID workoutId,
            @Valid @RequestBody final BulkUpdateExercisesRequest request) {

        workoutService.updateWorkoutExercises(workoutId, userId, request.exercises());
        return ResponseEntity.noContent().build();
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

    public record WorkoutResponseDTO(
            UUID id,
            String name,
            List<WorkoutExerciseDTO> exercises
    ) {}

    public record WorkoutExerciseDTO(
            UUID id,
            UUID exerciseId,
            String exerciseName,
            Integer targetSets,
            Integer targetReps,
            BigDecimal baseWeight,
            Integer orderIndex
    ) {}

    public record BulkUpdateExercisesRequest(
            @NotNull(message = "A lista de exercícios não pode ser nula.")
            List<ExerciseDetailRequest> exercises
    ) {}

    public record ExerciseDetailRequest(
            @NotNull(message = "O ID do exercício é obrigatório.")
            UUID exerciseId,

            @NotNull
            @Min(1)
            Integer targetSets,

            @NotNull
            @Min(1)
            Integer targetReps,

            @NotNull
            @Min(0)
            BigDecimal baseWeight,

            @NotNull
            Integer orderIndex
    ) {}
}