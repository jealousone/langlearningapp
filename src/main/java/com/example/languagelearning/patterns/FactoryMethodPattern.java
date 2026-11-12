package com.example.languagelearning.patterns;

public class FactoryMethodPattern {
    interface Exercise {
        String question();
    }

    abstract static class ExerciseCreator {
        abstract Exercise createExercise();
    }

    static class VocabularyCreator extends ExerciseCreator {
        @Override
        Exercise createExercise() {
            return () -> "Translate: hola";
        }
    }

    public static void main(String[] args) {
        Exercise exercise = new VocabularyCreator().createExercise();
        System.out.println(exercise.question());
    }
}
