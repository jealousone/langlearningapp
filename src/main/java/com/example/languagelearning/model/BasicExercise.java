package com.example.languagelearning.model;

import java.util.List;

public class BasicExercise implements Exercise {
    private final String id;
    private final ExerciseType type;
    private final String question;
    private final String correctAnswer;
    private final List<String> options;
    private final int points;

    public BasicExercise(String id, ExerciseType type, String question,
                         String correctAnswer, List<String> options, int points) {
        this.id = id;
        this.type = type;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.options = options == null ? List.of() : List.copyOf(options);
        this.points = points;
    }

    @Override public String getId() { return id; }
    @Override public ExerciseType getType() { return type; }
    @Override public String getQuestion() { return question; }
    @Override public String getCorrectAnswer() { return correctAnswer; }
    @Override public List<String> getOptions() { return options; }
    @Override public String getHint() { return "No hint available."; }
    @Override public int getPoints() { return points; }
}
