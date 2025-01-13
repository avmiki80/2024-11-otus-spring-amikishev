package ru.otus.spring.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.dao.QuestionDaoImpl;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.domain.Question;

@TestConfiguration
public class DaoConfigTest{
    @MockBean
    private ResourceLoader<Question> resourceLoader;
    @Bean
    public QuestionDaoImpl questionDao(){
        return new QuestionDaoImpl(resourceLoader);
    }
}
