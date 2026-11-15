package com.example.languagelearning.progress_patterns.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

public class ProgressSubject {
    private final List<ProgressListener> listeners = new ArrayList<>();

    public void addListener(ProgressListener listener) {
        listeners.add(listener);
    }
    public void removeListener(ProgressListener listener) {
        listeners.remove(listener);
    }
    public void notifyListeners(String exerciseId, boolean correct, int earnedPoints) {
        for (ProgressListener listener : List.copyOf(listeners)){
            listener.onExerciseCompleted(exerciseId, correct, earnedPoints);
        }
    }
}
