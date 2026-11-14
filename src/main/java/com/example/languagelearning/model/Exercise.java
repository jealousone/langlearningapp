package com.example.languagelearning.model;

public interface Exercise {
    String getPrompt();
    String getExpectedAnswer();
    ExerciseType getType();
}
