package com.example.languagelearning.service;

import com.example.languagelearning.builder.LessonBuilder;
import com.example.languagelearning.factory.ExerciseCreator;
import com.example.languagelearning.factory.MultipleChoiceExerciseCreator;
import com.example.languagelearning.factory.TranslationExerciseCreator;
import com.example.languagelearning.factory.VocabularyExerciseCreator;
import com.example.languagelearning.model.Exercise;
import com.example.languagelearning.model.ExerciseType;
import com.example.languagelearning.model.Lesson;
import com.example.languagelearning.progress_patterns.decorator.BonusPointDecorator;
import com.example.languagelearning.progress_patterns.decorator.HintDecorator;
import com.example.languagelearning.progress_patterns.observer.AchievementObserver;
import com.example.languagelearning.progress_patterns.observer.ProgressObserver;
import com.example.languagelearning.progress_patterns.observer.ProgressSubject;
import com.example.languagelearning.strategy.AnswerStrategy;
import com.example.languagelearning.strategy.ExactAnswerStrategy;
import com.example.languagelearning.strategy.MultipleChoiceAnswerStrategy;
import com.example.languagelearning.strategy.TranslationAnswerStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppService {
    private final Map<String, Lesson> lessons = new LinkedHashMap<>();
    private final AnswerStrategy exactStrategy = new ExactAnswerStrategy();
    private final AnswerStrategy translationStrategy = new TranslationAnswerStrategy();
    private final AnswerStrategy choiceStrategy = new MultipleChoiceAnswerStrategy();

    private final ProgressSubject progressSubject = new ProgressSubject();
    private final ProgressObserver progressObserver;
    private final AchievementObserver achievementObserver;
    private final Map<String, Boolean> completedLessons = new HashMap<>();

    public AppService(ProgressObserver progressObserver, AchievementObserver achievementObserver) {
        this.progressObserver = progressObserver;
        this.achievementObserver = achievementObserver;
        progressSubject.addListener(progressObserver);
        progressSubject.addListener(achievementObserver);
    }

    @PostConstruct
    public void loadSampleLessons() {
        ExerciseCreator vocabCreator = new VocabularyExerciseCreator();
        ExerciseCreator multChoiceCreator = new MultipleChoiceExerciseCreator();
        ExerciseCreator translationCreator = new TranslationExerciseCreator();

        Exercise e1 = new HintDecorator(
                vocabCreator.createExercise("ex1", "What is 'hello' in Spanish?",
                        "Hola", List.of(), 10),
                "It starts with the letter H.");

        Exercise e2 = translationCreator.createExercise(
                "ex2", "Translate 'Good morning' into Spanish.",
                "Buenos días", List.of(), 10);

        Exercise e3 = new BonusPointDecorator(
                multChoiceCreator.createExercise("ex3", "Choose the Spanish word for 'water'.",
                        "Agua", List.of("Leche", "Agua", "Pan", "Casa"), 10),
                5);

        Exercise e4 = vocabCreator.createExercise(
                "ex4", "What is 'thank you' in Spanish?",
                "Gracias", List.of(), 10);

        Exercise e5 = translationCreator.createExercise(
                "ex5", "Translate 'Good night' into Spanish.",
                "Buenas noches", List.of(), 10);

        Lesson basics = new LessonBuilder()
                .id("lesson1")
                .title("Spanish Basics")
                .language("Spanish")
                .description("Learn five useful everyday Spanish expressions.")
                .addExercise(e1)
                .addExercise(e2)
                .addExercise(e3)
                .addExercise(e4)
                .addExercise(e5)
                .build();
        lessons.put(basics.getId(), basics);

        Exercise e6 = multChoiceCreator.createExercise(
                "ex6", "Choose the Spanish word for 'book'.",
                "Libro", List.of("Libro", "Perro", "Agua", "Rojo"), 10);

        Exercise e7 = vocabCreator.createExercise(
                "ex7", "What is 'house' in Spanish?", "Casa", List.of(), 10);

        Exercise e8 = translationCreator.createExercise(
                "ex8", "Translate 'I love learning'.",
                "Me encanta aprender", List.of(), 15);
    }

    public List<Lesson> getLessons() {
        return new ArrayList<>(lessons.values());
    }

    public Lesson getLesson(String id) {
        Lesson lesson = lessons.get(id);
        if (lesson == null) {
            throw new IllegalArgumentException("Lesson not found");
        } else {
            return lesson;
        }
    }

    public Map<String, Object> completeLesson(String lessonId, Map<String, String> answers) {
        Lesson lesson = getLesson(lessonId);
        int score = 0;
        int correctCount = 0;
        List<Map<String, Object>> results = new ArrayList<>();

        for (Exercise exercise : lesson.getExercises()) {
            String answer = answers == null ? null : answers.get(exercise.getId());
            boolean correct = getStrategy(exercise.getType())
                    .checkAnswer(answer, exercise.getCorrectAnswer());
            int earned = correct ? exercise.getPoints() : 0;

            if (correct) {
                score += earned;
                correctCount++;
            }

            progressSubject.notifyListeners(exercise.getId(), correct, earned);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("exerciseId", exercise.getId());
            item.put("correct", correct);
            item.put("correctAnswer", exercise.getCorrectAnswer());
            item.put("userAnswer", answer == null ? "" : answer);
            item.put("earnedPoints", earned);
            results.add(item);
        }

        completedLessons.put(lessonId, true);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("lessonTitle", lesson.getTitle());
        response.put("score", score);
        response.put("correctCount", correctCount);
        response.put("totalExercises", lesson.getExercises().size());
        response.put("results", results);
        return response;
    }

    private AnswerStrategy getStrategy(ExerciseType type) {
        return switch (type) {
            case VOCABULARY -> exactStrategy;
            case TRANSLATION -> translationStrategy;
            case MULTIPLE_CHOICE -> choiceStrategy;
        };
    }

    public Map<String, Object> getProgress() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalCompleted", progressObserver.getTotalCompleted());
        result.put("totalCorrect", progressObserver.getTotalCorrect());
        result.put("totalPoints", progressObserver.getTotalPoints());
        result.put("completedLessons", completedLessons.size());
        result.put("totalLessons", lessons.size());
        result.put("achievements", achievementObserver.getAchievements());
        return result;
    }

    public List<Map<String, Object>> getLessonCards() {
        List<Map<String, Object>> cards = new ArrayList<>();
        for (Lesson lesson : lessons.values()) {
            Map<String, Object> card = new LinkedHashMap<>();
            card.put("id", lesson.getId());
            card.put("title", lesson.getTitle());
            card.put("language", lesson.getLanguage());
            card.put("description", lesson.getDescription());
            card.put("exerciseCount", lesson.getExercises().size());
            card.put("completed", completedLessons.containsKey(lesson.getId()));
            cards.add(card);
        }
        return cards;
    }

    public Map<String, Object> getLessonForWebsite(String id) {
        Lesson lesson = getLesson(id);
        List<Map<String, Object>> exercises = new ArrayList<>();
        for (Exercise exercise : lesson.getExercises()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", exercise.getId());
            item.put("type", exercise.getType().toString());
            item.put("question", exercise.getQuestion());
            item.put("options", exercise.getOptions());
            item.put("hint", exercise.getHint());
            item.put("points", exercise.getPoints());
            exercises.add(item);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", lesson.getId());
        result.put("title", lesson.getTitle());
        result.put("language", lesson.getLanguage());
        result.put("description", lesson.getDescription());
        result.put("exercises", exercises);
        return result;
    }
}
