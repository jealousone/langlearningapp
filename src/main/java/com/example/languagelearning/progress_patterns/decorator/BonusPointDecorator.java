package com.example.languagelearning.progress_patterns.decorator;

import com.example.languagelearning.model.Exercise;

public class BonusPointDecorator extends ExerciseDecorator{
    private final int bonusPoints;

    public BonusPointDecorator(Exercise exercise, int bonusPoints) {
        super(exercise);
        this.bonusPoints = bonusPoints;
    }

    @Override
    public int getPoints() {
        return wrappedExercise.getPoints() + bonusPoints;
    }

}
