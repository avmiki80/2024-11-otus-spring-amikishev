package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.service.ReadWriteService;
import ru.otus.spring.service.examing.*;

@PropertySource("classpath:application-test.properties")
@TestConfiguration
public class ServiceConfigTest {
    @Value("${threshold_correct_answer}")
    Long thresholdCorrectAnswer;

    @MockBean
    private QuestionDao questionDao;
    @MockBean
    private CheckQuestionService checkQuestionService;

    @MockBean
    private ReadWriteService readWriteService;
    @Bean
    public TestingService testingService(){
        return new TestingServiceImpl(checkQuestionService, questionDao);
    }
    @Bean
    public ExaminatorService examinatorService(){
        return new ExaminatorServiceImpl(testingService(), rateService(), readWriteService);
    }
    @Bean
    public RateService rateService(){
        return new RateServiceImpl(thresholdCorrectAnswer);
    }
    @Bean
    public RateService wrongRateService(){
        return new RateServiceImpl(null);
    }
}
