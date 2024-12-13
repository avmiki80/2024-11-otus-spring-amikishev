package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceConfigTest;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.dao.QuestionDaoImpl;
import ru.otus.spring.dao.ResourceLoader;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.service.examing.CheckQuestionService;
import ru.otus.spring.service.examing.CheckQuestionServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static ru.otus.spring.message.ServiceMessage.QUESTION_NOT_FOUND;

@ContextConfiguration(classes = QuestionServiceImplTest.QuestionServiceConfig.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс QuestionServiceImpl")
class QuestionServiceImplTest {
    @Autowired
    private ResourceLoader<Question> resourceLoader;
    @Autowired
    private QuestionDaoImpl questionDao;
    @Autowired
    private QuestionService questionService;
    @BeforeEach
    public void init(){
        when(resourceLoader.load()).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new Question("q1", "a1"),
                        new Question("q2", "a2"),
                        new Question("q3", "a3")
                ))
        );
        questionDao.init();;
    }
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(questionService).isNotNull();
    }
    @DisplayName("Проверка получения всех вопросов")
    @Test
    public void whenFindAllQuestion_thenReturnAllQuestions(){
        List<Question> questions = questionService.findAll();
        assertAll(
                () -> assertThat(questions).isNotNull(),
                () -> assertThat(questions).isNotEmpty(),
                () -> assertThat(questions).hasSize(3)
        );
    }
    @DisplayName("Проверка получения конкретного вопроса")
    @Test
    public void whenGetQuestion_thenReturnQuestion(){
        Question question = questionService.get("q2");
        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question).isEqualTo(new Question("q2", "a2")),
                () -> assertThat(question.getQuestion()).isEqualTo("q2")
        );
    }
    @DisplayName("Проверка выброса исключения если вопрос не обнаружен")
    @Test
    public void whenGetQuestion_thenThrowsException(){
        ServiceException exception = assertThrows(ServiceException.class, () -> questionService.get("q4"));
        assertThat(exception.getMessage()).isEqualTo(QUESTION_NOT_FOUND);
    }

    @DisplayName("Проверка удаления конкретного вопроса")
    @Test
    public void whenDeleteQuestion_thenQuestionsSizeSmaller(){
        int baforeRemoveSize = questionService.findAll().size();
        Question removed = new Question("q2", "a2");
        questionService.remove(removed);
        List<Question> afterRemove = questionService.findAll();
        assertAll(
                () -> assertThat(afterRemove.size()).isEqualTo(baforeRemoveSize - 1),
                () -> assertThat(afterRemove).doesNotContain(removed)
        );
    }
    @DisplayName("Проверка выброса исключения если удаляемый вопрос не обнаружен")
    @Test
    public void wheDeleteQuestion_thenThrowsException(){
        ServiceException exception = assertThrows(ServiceException.class, () -> questionService.remove(new Question("q4", "a4")));
        assertThat(exception.getMessage()).isEqualTo(QUESTION_NOT_FOUND);
    }
    @DisplayName("Проверка добавления нового вопроса")
    @Test
    public void whenSaveQuestion_thenQuestionsSizeBiger(){
        int baforeSaveeSize = questionService.findAll().size();
        Question added = new Question("q4", "a4");
        questionService.save(added);
        List<Question> afterSave = questionService.findAll();
        assertAll(
                () -> assertThat(afterSave.size()).isEqualTo(baforeSaveeSize + 1),
                () -> assertThat(afterSave).contains(added)
        );
    }

    @DisplayName("Проверка изменение вопроса")
    @Test
    public void whenSaveExistQuestion_thenQuestionsSizeNotChanged(){
        int baforeSaveeSize = questionService.findAll().size();
        Question added = new Question("q2", "a4");
        questionService.save(added);
        List<Question> afterSave = questionService.findAll();
        assertAll(
                () -> assertThat(afterSave.size()).isEqualTo(baforeSaveeSize),
                () -> assertThat(afterSave).contains(added)
        );
    }
    @TestConfiguration
    static class QuestionServiceConfig{
        @MockBean
        private ResourceLoader<Question> resourceLoader;
        @Bean
        public QuestionDao questionDao(){
            return new QuestionDaoImpl(resourceLoader);
        }
        @Bean
        public QuestionService questionService(){
            return new QuestionServiceImpl(questionDao());
        }
    }
}