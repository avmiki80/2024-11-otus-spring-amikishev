package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
@PropertySource("classpath:application.properties")
@Configuration
public class ResourceConfig {

    private String questionsFilename;

    public ResourceConfig(@Value("${questions_filename}") String questionsFilename) {
        this.questionsFilename = questionsFilename;
    }

    @Bean
    public Resource resource(){
        return new ClassPathResource(questionsFilename);
    }
}
