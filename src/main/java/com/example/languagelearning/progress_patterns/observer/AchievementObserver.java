package com.example.languagelearning.progress_patterns.observer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
@Component
public class AchievementObserver implements ProgressListener{
    private final List<String> achievements = new ArrayList<>();

    @Override
    public void onExerciseCompleted(String exerciseId, boolean correct, int earnedPoints) {
        if(correct && !achievements.contains("First correct answer")){
            achievements.add("First correct answer");
        }
        if(correct && !achievements.contains("Point collector")){
            achievements.add("Point collector");
        }
        if (achievements.size() >= 2 && !achievements.contains("Rising learner")) {
            achievements.add("Rising learner");
        }
    }

    public List<String> getAchievements() {
        return achievements;
    }
}
