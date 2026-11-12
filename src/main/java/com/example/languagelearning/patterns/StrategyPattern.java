package com.example.languagelearning.patterns;

public class StrategyPattern {
    interface AnswerStrategy {
        boolean isCorrect(String expected, String answer);
    }

    static class IgnoreCaseStrategy implements AnswerStrategy {
        @Override
        public boolean isCorrect(String expected, String answer) {
            return answer != null && expected.equalsIgnoreCase(answer.trim());
        }
    }

    public static void main(String[] args) {
        AnswerStrategy strategy = new IgnoreCaseStrategy();
        System.out.println(strategy.isCorrect("hello", "HELLO"));
    }
}
