package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.dao.ResourceLoaderImpl;
import ru.otus.spring.domain.Question;

@PropertySource("classpath:application.properties")
@TestConfiguration
public class ResourceLoaderConfigTest{
    @Value("${questions_filename}")
    private String questionsFilename;
    @Bean
    public ResourceLoader<Question> correctResourceLoader() {
        return new ResourceLoaderImpl(questionsFilename);
    }
    @Bean
    public ResourceLoader<Question> wrongResourceLoader() {
        return new ResourceLoaderImpl("wrongFileName");
    }
}
