package ru.otus.spring.service.examing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceServiceConfigTest;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = ServiceServiceConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс TestingServiceImpl")
class TestingServiceImplTest {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private CheckQuestionService checkQuestionService;

    @Autowired
    private TestingService testingService;

    private List<Question> questions;
    @BeforeEach
    public void init(){
        questions =
                Arrays.asList(
                        new Question("q1", "a1"),
                        new Question("q2", "a2"),
                        new Question("q3", "a3"));
    }
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(testingService).isNotNull();
    }
    @DisplayName("Проверка, если все ответы не правильные")
    @Test
    public void whenAllAnswersIsWrong_thenCountTrueAnswerIsZero(){
        when(checkQuestionService.checkQuestion(any())).thenReturn(false);
        when(questionDao.findAll()).thenReturn(questions);
        assertAll(
                () -> assertDoesNotThrow(() -> this.testingService.testing()),
                () -> assertThat(this.testingService.testing()).isEqualTo(0L)
        );
    }

    @DisplayName("Проверка, если все ответы правильные")
    @Test
    public void whenQuestionsIsTwo_thenCountTrueAnswerIsTwo(){
        when(checkQuestionService.checkQuestion(any())).thenReturn(true);
        when(questionDao.findAll()).thenReturn(questions);

        assertAll(
                () -> assertDoesNotThrow(() -> this.testingService.testing()),
                () -> assertThat(this.testingService.testing()).isEqualTo(3L)
        );
    }
    @DisplayName("Проверка, если один правильный ответ")
    @Test
    public void whenOneTrueQuestions_thenCountTrueAnswerIsOne(){
        when(checkQuestionService.checkQuestion(new Question("q2", "a2"))).thenReturn(true);
        given(questionDao.findAll()).willReturn(questions);

        assertAll(
                () -> assertDoesNotThrow(() -> this.testingService.testing()),
                () -> assertThat(this.testingService.testing()).isEqualTo(1L)
        );
    }

}