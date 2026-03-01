package com.liftai.backend.controllers;

import com.liftai.backend.entities.Exercise;
import com.liftai.backend.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    // Retorna todos os exercícios do catálogo
    @GetMapping
    public ResponseEntity<List<Exercise>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.findAllExercises());
    }

    // Busca exercícios filtrando pela categoria (ex: /api/exercises/category/Peito)
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Exercise>> getExercisesByCategory(@PathVariable final String category) {
        return ResponseEntity.ok(exerciseService.findExercisesByCategory(category));
    }
}