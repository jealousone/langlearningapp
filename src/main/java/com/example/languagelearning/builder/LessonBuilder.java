package com.example.languagelearning.builder;

import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonBuilder {
    private String title;
    private String language;
    private final List<Exercise> exercises = new ArrayList<>();

    public LessonBuilder title(String title) {
        this.title = title;
        return this;
    }

    public LessonBuilder language(String language) {
        this.language = language;
        return this;
    }

    public LessonBuilder addExercise(Exercise exercise) {
        exercises.add(exercise);
        return this;
    }

    public Lesson build() {
        if (title == null || language == null || exercises.isEmpty()) {
            throw new IllegalStateException("A lesson needs a title, language and at least one exercise");
        }
        return new Lesson(title, language, exercises);
    }
}
