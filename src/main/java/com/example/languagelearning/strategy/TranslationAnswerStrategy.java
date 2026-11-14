package com.example.languagelearning.strategy;

public class TranslationAnswerStrategy implements AnswerStrategy {
    @Override
    public boolean checkAnswer(String userAnswer, String correctAnswer) {
        if (userAnswer == null || correctAnswer == null) return false;
        String normalizedUser = userAnswer.trim().toLowerCase().replaceAll("\\s+", " ");
        String normalizedCorrect = correctAnswer.trim().toLowerCase().replaceAll("\\s+", " ");
        return normalizedUser.equals(normalizedCorrect);
    }
}
