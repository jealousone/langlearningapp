package com.example.languagelearning.strategy;

public class ExactAnswerStrategy implements AnswerStrategy {
    @Override
    public boolean isCorrect(String expectedAnswer, String answer) {
        return answer != null && expectedAnswer.equals(answer.trim());
    }
}
