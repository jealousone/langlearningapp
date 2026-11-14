package com.example.languagelearning.strategy;

public class MultipleChoiceAnswerStrategy implements AnswerStrategy {
    @Override
    public boolean isCorrect(String expectedAnswer, String answer) {
        return answer != null && expectedAnswer.equalsIgnoreCase(answer.trim());
    }
}
