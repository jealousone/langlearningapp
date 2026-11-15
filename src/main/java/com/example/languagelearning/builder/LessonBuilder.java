package com.example.languagelearning.builder;

import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.Lesson;
import java.util.ArrayList;
import java.util.List;

public class LessonBuilder {
    private String id;
    private String title;
    private String language;
    private String description = "";
    private final List<Exercise> exercises = new ArrayList<>();

    public LessonBuilder id(String id) {
        this.id = id;
        return this;
    }

    public LessonBuilder title(String title) {
        this.title = title;
        return this;
    }

    public LessonBuilder language(String language) {
        this.language = language;
        return this;
    }

    public LessonBuilder description(String description) {
        this.description = description;
        return this;
    }

    public LessonBuilder addExercise(Exercise exercise) {
        exercises.add(exercise);
        return this;
    }

    public Lesson build() {
        if (id == null || title == null || language == null) {
            throw new IllegalStateException("Lesson id, title and language are required.");
        }
        if (exercises.isEmpty()) {
            throw new IllegalStateException("A lesson must contain at least one exercise.");
        }
        return new Lesson(id, title, language, description, exercises);
    }
}
