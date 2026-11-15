package com.example.languagelearning.progress_patterns.singleton;

public final class Settings {
    private static final Settings instance = new Settings();
    private String language = "Spanish";

    private Settings() {}

    public static Settings getInstance() {
        return instance;
    }

    public synchronized String getLanguage() {
        return language;
    }

    public synchronized void setLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException("Language cannot be empty.");
        }
        this.language = language.trim();
    }
}
