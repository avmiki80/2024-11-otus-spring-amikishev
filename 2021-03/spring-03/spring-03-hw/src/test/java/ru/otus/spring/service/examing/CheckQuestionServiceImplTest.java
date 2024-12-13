package ru.otus.spring.service.examing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceConfigTest;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.ReadWriteService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = CheckQuestionServiceImplTest.CheckQuestionServiceConfig.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс CheckQuestionServiceImpl")
class CheckQuestionServiceImplTest {
    @Autowired
    private CheckQuestionService checkQuestionService;
    @Autowired
    private ReadWriteService readWriteService;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(checkQuestionService).isNotNull();
    }
    @DisplayName("Проверка коректности вывода вопроса")
    @Test
    public void whenCorrectQuestion_thenShowMessage(){
        Question question = new Question("q1", "a1");
        when(readWriteService.read()).thenReturn("a1");
        checkQuestionService.checkQuestion(question);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(readWriteService).write(message.capture());

        assertThat(message.getValue()).isEqualTo(question.getQuestion() + "? ");

    }

    @DisplayName("Проверка коректности оценки правильного ответа")
    @Test
    public void whenCorrectAnswer_thenReturnTrue(){
        Question question = new Question("q1", "a1");
        when(readWriteService.read()).thenReturn("a1");
        checkQuestionService.checkQuestion(question);
        assertThat(checkQuestionService.checkQuestion(question)).isEqualTo(true);
    }
    @DisplayName("Проверка коректности оценки не правильного ответа")
    @Test
    public void whenWrongAnswer_thenReturnFalse(){
        Question question = new Question("q1", "a1");
        when(readWriteService.read()).thenReturn("a2");
        checkQuestionService.checkQuestion(question);
        assertThat(checkQuestionService.checkQuestion(question)).isEqualTo(false);
    }
    @TestConfiguration
    static class CheckQuestionServiceConfig{
        @MockBean
        private ReadWriteService readWriteService;
        @Bean
        public CheckQuestionService checkQuestionService(){
            return new CheckQuestionServiceImpl(readWriteService);
        }
    }
}