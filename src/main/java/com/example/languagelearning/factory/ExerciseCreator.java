package com.example.languagelearning.factory;

import com.example.languagelearning.model.Exercise;

public abstract class ExerciseCreator {
    public abstract Exercise create(String prompt, String expectedAnswer);
}
