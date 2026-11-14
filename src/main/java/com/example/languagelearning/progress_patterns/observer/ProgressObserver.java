package com.example.languagelearning.progress_patterns.observer;

import org.springframework.stereotype.Component;

@Component
public class ProgressObserver implements ProgressListener{
    private int totalCompleted;
    private int totalCorrect;
    private int totalPoints;

    @Override
    public void onExerciseCompleted(String exerciseId, boolean correct, int earnedPoints) {
        totalCompleted++;
        if(correct){
            totalCorrect++;
        }
        totalPoints += earnedPoints;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public int getTotalPoints() {
        return totalPoints;
    }
}
