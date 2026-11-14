package com.example.languagelearning.model;

import java.util.List;

public interface Exercise {
    String getId();
    ExerciseType getType();
    String getQuestion();
    String getCorrectAnswer();
    List<String> getOptions();
    String getHint();
    int getPoints();
}
