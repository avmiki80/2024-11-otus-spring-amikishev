package ru.otus.spring.service.examing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceServiceConfigTest;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.ReadWriteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ServiceServiceConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс ExaminatorServiceImpl")
class ExaminatorServiceImplTest {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private CheckQuestionService checkQuestionService;
    @Autowired
    private ExaminatorService examinatorService;
    @Autowired
    private ReadWriteService readWriteService;
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
        assertThat(examinatorService).isNotNull();
    }
    @DisplayName("Проверка вывода поздравления")
    @Test
    public void whenCorrectAllAnswers_thenCongratulation(){
        when(checkQuestionService.checkQuestion(any())).thenReturn(true);
        when(questionDao.findAll()).thenReturn(questions);

        examinatorService.examing("firstname", "lastname");

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(readWriteService, times(2)).write(message.capture());

        assertThat(new ArrayList<>(message.getAllValues())).contains("Результат: firstname lastname = 3", "Поздравляем");
    }
    @DisplayName("Проверка вывода попробовать еще")
    @Test
    public void whenWrongAllQuestions_thenTryItAgain(){
        when(checkQuestionService.checkQuestion(any())).thenReturn(false);
        when(questionDao.findAll()).thenReturn(questions);

        examinatorService.examing("firstname", "lastname");

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(readWriteService, times(2)).write(message.capture());

        assertThat(new ArrayList<>(message.getAllValues())).contains("Результат: firstname lastname = 0", "Попробуй еще");
    }
}