package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.dao.ResourceLoaderImpl;
import ru.otus.spring.domain.Question;

@PropertySource("classpath:application-test.properties")
@TestConfiguration
public class ResourceLoaderConfigTest {
    @Value("${questions_filename}")
    private String questionsFilename;
    @Bean
    public Resource wrongFileName(){
        return new ClassPathResource("wrongFileName");
    }
    @Bean
    public Resource correctFileName(){
        return new ClassPathResource(questionsFilename);
    }
    @Bean
    public ResourceLoader<Question> correctResourceLoader() {
        return new ResourceLoaderImpl(correctFileName());
    }
    @Bean
    public ResourceLoader<Question> wrongResourceLoader() {
        return new ResourceLoaderImpl(wrongFileName());
    }
}
