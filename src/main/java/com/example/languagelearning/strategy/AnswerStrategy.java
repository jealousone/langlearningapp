package com.example.languagelearning.strategy;

public interface AnswerStrategy {
    boolean isCorrect(String expectedAnswer, String answer);
}
