package com.liftai.backend.services;

import com.liftai.backend.entities.Exercise;
import com.liftai.backend.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public List<Exercise> findAllExercises() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> findExercisesByCategory(final String category) {
        log.info("Buscando exercícios pela categoria: {}", category);
        return exerciseRepository.findByCategory(category);
    }

    public Exercise findExerciseById(final UUID exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado."));
    }
}