package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoImpl;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.dao.ResourceLoaderImpl;
import ru.otus.spring.domain.Question;

@TestConfiguration
public class DaoConfigTest {

    @MockBean
    private ResourceLoader<Question> resourceLoader;
    @Bean
    public QuestionDao questionDao(){
        return new QuestionDaoImpl(resourceLoader);
    }
}
