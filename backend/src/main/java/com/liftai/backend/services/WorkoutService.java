package com.liftai.backend.services;

import com.liftai.backend.controllers.WorkoutController;
import com.liftai.backend.entities.User;
import com.liftai.backend.entities.Workout;
import com.liftai.backend.entities.WorkoutExercise;
import com.liftai.backend.repositories.ExerciseRepository;
import com.liftai.backend.repositories.WorkoutExerciseRepository;
import com.liftai.backend.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseRepository exerciseRepository;

    public Workout findWorkoutByIdAndUser(final UUID workoutId, final UUID userId) {
        final var workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Treino não encontrado."));

        if (!workout.getUser().getId().equals(userId)) {
            throw new RuntimeException("Acesso negado. Este treino pertence a outro usuário.");
        }

        return workout;
    }

    @Transactional
    public Workout createWorkout(final UUID userId, final String name) {
        log.info("Criando novo treino '{}' para o usuário ID: {}", name, userId);

        final var userRef = User.builder().id(userId).build();

        final var newWorkout = Workout.builder()
                .user(userRef)
                .name(name)
                .build();

        return workoutRepository.save(newWorkout);
    }

    @Transactional
    public WorkoutExercise addExerciseToWorkout(
            final UUID workoutId,
            final UUID exerciseId,
            final Integer targetSets,
            final Integer targetReps,
            final BigDecimal baseWeight) {

        log.info("Adicionando exercício ID {} ao treino ID {}", exerciseId, workoutId);

        final var workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException("Treino não encontrado."));

        final var exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado."));

        final var currentExercises = workoutExerciseRepository.findByWorkoutIdOrderByOrderIndexAsc(workoutId);
        final var nextOrderIndex = currentExercises.isEmpty() ? 1 : currentExercises.get(currentExercises.size() - 1).getOrderIndex() + 1;

        final var newWorkoutExercise = WorkoutExercise.builder()
                .workout(workout)
                .exercise(exercise)
                .targetSets(targetSets)
                .targetReps(targetReps)
                .baseWeight(baseWeight)
                .orderIndex(nextOrderIndex)
                .build();

        return workoutExerciseRepository.save(newWorkoutExercise);
    }

    @Transactional
    public void updateWorkoutExercises(final UUID workoutId, final UUID userId, final List<WorkoutController.ExerciseDetailRequest> requests) {

        final var workout = findWorkoutByIdAndUser(workoutId, userId);

        workoutExerciseRepository.deleteByWorkoutId(workoutId);

        final var newExercises = requests.stream().map(req -> {
            final var exercise = exerciseRepository.findById(req.exerciseId())
                    .orElseThrow(() -> new RuntimeException("Exercício base não encontrado: " + req.exerciseId()));

            return WorkoutExercise.builder()
                    .workout(workout)
                    .exercise(exercise)
                    .targetSets(req.targetSets())
                    .targetReps(req.targetReps())
                    .baseWeight(req.baseWeight())
                    .orderIndex(req.orderIndex())
                    .build();
        }).toList();

        workoutExerciseRepository.saveAll(newExercises);
    }

    @Transactional
    public void deleteWorkout(final UUID workoutId, final UUID userId) {
        final var workout = findWorkoutByIdAndUser(workoutId, userId);

        workoutRepository.delete(workout);
    }

    public List<Workout> findWorkoutsByUser(final UUID userId) {
        return workoutRepository.findByUserId(userId);
    }
}