package com.example.languagelearning.config;

import com.example.languagelearning.progress_patterns.singleton.Settings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public Settings appSettings() {
        return Settings.getInstance();
    }
}
