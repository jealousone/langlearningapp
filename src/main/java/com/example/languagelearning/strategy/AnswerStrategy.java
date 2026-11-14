package com.example.languagelearning.strategy;

public interface AnswerStrategy {
    boolean checkAnswer(String userAnswer, String correctAnswer);
}
