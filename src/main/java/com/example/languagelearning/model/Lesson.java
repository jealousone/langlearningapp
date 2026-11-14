package com.example.languagelearning.model;

import java.util.List;

public class Lesson {
    private final String id;
    private final String title;
    private final String language;
    private final String description;
    private final List<Exercise> exercises;

    public Lesson(String id, String title, String language,
                  String description, List<Exercise> exercises) {
        this.id = id;
        this.title = title;
        this.language = language;
        this.description = description;
        this.exercises = List.copyOf(exercises);
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getLanguage() { return language; }
    public String getDescription() { return description; }
    public List<Exercise> getExercises() { return exercises; }
}
