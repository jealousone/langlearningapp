package com.example.languagelearning.progress_patterns.observer;

public interface ProgressListener {
    void onExerciseCompleted(String exerciseId, boolean correct, int earnedPoints);
}
