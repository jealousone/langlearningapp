package com.example.languagelearning.strategy;

public class TranslationAnswerStrategy implements AnswerStrategy {
    @Override
    public boolean isCorrect(String expectedAnswer, String answer) {
        return answer != null && normalize(expectedAnswer).equals(normalize(answer));
    }

    private String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[.!?]$", "");
    }
}
