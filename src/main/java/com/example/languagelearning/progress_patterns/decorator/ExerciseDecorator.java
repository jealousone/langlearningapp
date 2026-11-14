package com.example.languagelearning.progress_patterns.decorator;

import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.ExerciseType;

import java.util.List;

public abstract class ExerciseDecorator implements Exercise {
    protected final Exercise wrappedExercise;

    public ExerciseDecorator(Exercise exercise) {
        this.wrappedExercise = exercise;
    }

    @Override public String getId() { return wrappedExercise.getId(); }
    @Override public ExerciseType getType() { return wrappedExercise.getType(); }
    @Override public String getQuestion() { return wrappedExercise.getQuestion(); }
    @Override public String getCorrectAnswer() { return wrappedExercise.getCorrectAnswer(); }
    @Override public List<String> getOptions() { return wrappedExercise.getOptions(); }
    @Override public String getHint() { return wrappedExercise.getHint(); }
    @Override public int getPoints() { return wrappedExercise.getPoints(); }
}
