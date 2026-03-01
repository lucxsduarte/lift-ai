package com.liftai.backend.controllers;

import com.liftai.backend.entities.Exercise;
import com.liftai.backend.enums.ExerciseCategory;
import com.liftai.backend.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<List<Exercise>> getExercisesByCategory(@PathVariable Integer categoryCode) {
        return ResponseEntity.ok(exerciseService.findExercisesByCategory(categoryCode));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        final var categories = Arrays.stream(ExerciseCategory.values())
                .map(category -> new CategoryDTO(category.getCode(), category.getDescription()))
                .toList();

        return ResponseEntity.ok(categories);
    }

    public record CategoryDTO(int code, String description) {}
}