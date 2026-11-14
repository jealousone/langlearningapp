package com.example.languagelearning.progress_patterns.decorator;

import com.example.languagelearning.model.Exercise;

public class HintDecorator extends ExerciseDecorator{
    private final String hint;

    public HintDecorator(Exercise exercise) {
        super(exercise);
        this.hint = exercise.getHint();
    }

    public String getHint() {
        return hint;
    }
}
