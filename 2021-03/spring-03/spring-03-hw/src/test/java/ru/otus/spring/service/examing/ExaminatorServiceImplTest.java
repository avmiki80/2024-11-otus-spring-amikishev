package ru.otus.spring.service.examing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.service.ReadWriteService;
import ru.otus.spring.service.examing.CheckQuestionService;
import ru.otus.spring.service.examing.ExaminatorService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.otus.spring.message.ServiceMessage.*;

@ContextConfiguration(classes = ServiceConfigTest.class)
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
        when(readWriteService.read()).thenReturn("test");
        when(checkQuestionService.checkQuestion(any())).thenReturn(true);
        when(questionDao.findAll()).thenReturn(questions);

        examinatorService.examing();

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(readWriteService, times(4)).write(message.capture());

        assertThat(message.getAllValues().stream().skip(2).collect(Collectors.toList())).contains("Result test test = 3", CONGRATULATION);
    }
    @DisplayName("Проверка вывода попробовать еще")
    @Test
    public void whenWrongAllQuestions_thenTryItAgain(){
        when(readWriteService.read()).thenReturn("test");
        when(checkQuestionService.checkQuestion(any())).thenReturn(false);
        when(questionDao.findAll()).thenReturn(questions);

        examinatorService.examing();

        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(readWriteService, times(4)).write(message.capture());

        assertThat(message.getAllValues().stream().skip(2).collect(Collectors.toList())).contains("Result test test = 0", TRY_IT_AGAIN);
    }
}