package com.liftai.backend.enums;

import lombok.Getter;

@Getter
public enum ExerciseCategory {
    BACK(0, "Costas"),
    CHEST(1, "Peito"),
    LEGS(2, "Pernas"),
    SHOULDERS(3, "Ombros"),
    ARMS(4, "Braços"),
    CORE(5, "Abdômen"),
    CARDIO(6, "Cardio");

    private final int code;
    private final String description;

    ExerciseCategory(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ExerciseCategory fromCode(int code) {
        for (var category : ExerciseCategory.values()) {
            if (category.getCode() == code) {
                return category;
            }
        }

        throw new IllegalArgumentException("Código de categoria inválido: " + code);
    }
}