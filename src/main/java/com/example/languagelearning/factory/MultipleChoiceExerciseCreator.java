package com.example.languagelearning.factory;

import com.example.languagelearning.model.BasicExercise;
import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.ExerciseType;
import java.util.List;

public class MultipleChoiceExerciseCreator extends ExerciseCreator {
    @Override
    public Exercise createExercise(String id, String question, String answer,
                                   List<String> options, int points) {
        return new BasicExercise(id, getExerciseType(), question, answer, options, points);
    }

    @Override
    protected ExerciseType getExerciseType() {
        return ExerciseType.MULTIPLE_CHOICE;
    }
}
