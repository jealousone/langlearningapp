package com.example.languagelearning.strategy;

public class ExactAnswerStrategy implements AnswerStrategy {
    @Override
    public boolean checkAnswer(String userAnswer, String correctAnswer) {
        if (userAnswer == null || correctAnswer == null) return false;
        return userAnswer.trim().equalsIgnoreCase(correctAnswer.trim());
    }
}
