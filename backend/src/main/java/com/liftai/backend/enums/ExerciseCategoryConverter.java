package com.liftai.backend.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ExerciseCategoryConverter implements AttributeConverter<ExerciseCategory, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ExerciseCategory category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public ExerciseCategory convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return ExerciseCategory.fromCode(code);
    }
}