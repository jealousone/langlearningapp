package com.example.languagelearning.model;

public class BasicExercise implements Exercise {
    private final String prompt;
    private final String expectedAnswer;
    private final ExerciseType type;

    public BasicExercise(String prompt, String expectedAnswer, ExerciseType type) {
        this.prompt = prompt;
        this.expectedAnswer = expectedAnswer;
        this.type = type;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String getExpectedAnswer() {
        return expectedAnswer;
    }

    @Override
    public ExerciseType getType() {
        return type;
    }
}
