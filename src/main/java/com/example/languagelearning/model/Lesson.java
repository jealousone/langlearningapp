package com.example.languagelearning.model;

import java.util.List;

public class Lesson {
    private final String title;
    private final String language;
    private final List<Exercise> exercises;

    public Lesson(String title, String language, List<Exercise> exercises) {
        this.title = title;
        this.language = language;
        this.exercises = List.copyOf(exercises);
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }
}
