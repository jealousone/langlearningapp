package com.example.languagelearning.factory;

import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.ExerciseType;
import java.util.List;

// Factory Method: subclasses decide which concrete Exercise to create.
public abstract class ExerciseCreator {
    public abstract Exercise createExercise(
            String id, String question, String answer,
            List<String> options, int points);

    protected abstract ExerciseType getExerciseType();
}
