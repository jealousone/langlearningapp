package com.example.languagelearning.factory;

import com.example.languagelearning.model.BasicExercise;
import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.ExerciseType;

public class TranslationExerciseCreator extends ExerciseCreator {
    @Override
    public Exercise create(String prompt, String expectedAnswer) {
        return new BasicExercise(prompt, expectedAnswer, ExerciseType.TRANSLATION);
    }
}
