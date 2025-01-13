package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.dao.ResourceLoaderImpl;
import ru.otus.spring.domain.Question;

@PropertySource("classpath:application.properties")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
@Configuration
public class ResourceConfig {
    private final String questionsFilename;
    public ResourceConfig(
            @Value("${questions_filename}") String questionsFilename
    ) {
        this.questionsFilename = questionsFilename;
    }
    @Bean
    public ResourceLoader<Question> resourceLoader(){
        return new ResourceLoaderImpl(questionsFilename);
    }

}
