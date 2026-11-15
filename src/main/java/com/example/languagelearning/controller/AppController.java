package com.example.languagelearning.controller;

import com.example.languagelearning.progress_patterns.singleton.Settings;
import com.example.languagelearning.service.AppService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500",})
public class AppController {
    private final AppService appService;
    private final Settings appSettings;

    public AppController(AppService appService, Settings appSettings) {
        this.appService = appService;
        this.appSettings = appSettings;
    }

    @GetMapping("/lessons")
    public Object getLessons() {
        return appService.getLessonCards();
    }

    @GetMapping("/lessons/{id}")
    public Object getLesson(@PathVariable String id) {
        return appService.getLessonForWebsite(id);
    }

    @PostMapping("/lessons/{id}/complete")
    public Object completeLesson(@PathVariable String id,
                                 @RequestBody Map<String, Map<String, String>> body) {
        return appService.completeLesson(id, body.get("answers"));
    }

    @GetMapping("/progress")
    public Object getProgress() {
        return appService.getProgress();
    }

    @GetMapping("/settings")
    public Object getSettings() {
        return Map.of("language", appSettings.getLanguage());
    }

    @PutMapping("/settings/language")
    public Object updateLanguage(@RequestBody Map<String, String> body) {
        appSettings.setLanguage(body.get("language"));
        return Map.of("language", appSettings.getLanguage());
    }
}
