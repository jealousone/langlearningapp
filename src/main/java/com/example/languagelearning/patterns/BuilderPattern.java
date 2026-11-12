package com.example.languagelearning.patterns;

public class BuilderPattern {
    record Lesson(String language, String title) {}

    static class LessonBuilder {
        private String language;
        private String title;

        LessonBuilder language(String language) {
            this.language = language;
            return this;
        }

        LessonBuilder title(String title) {
            this.title = title;
            return this;
        }

        Lesson build() {
            return new Lesson(language, title);
        }
    }

    public static void main(String[] args) {
        Lesson lesson = new LessonBuilder()
                .language("Spanish")
                .title("Greetings")
                .build();
        System.out.println(lesson);
    }
}
