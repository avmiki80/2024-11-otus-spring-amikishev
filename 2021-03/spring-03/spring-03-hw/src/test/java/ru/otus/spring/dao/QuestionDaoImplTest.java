package ru.otus.spring.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.DaoConfigTest;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static ru.otus.spring.message.ServiceMessage.PROBLEM_RESOURCE;
import static ru.otus.spring.message.ServiceMessage.QUESTION_NOT_FOUND;

@ContextConfiguration(classes = DaoConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс QuestionDaoImpl")
class QuestionDaoImplTest {
    @Autowired
    private ResourceLoader<Question> resourceLoader;
    @Autowired
    private QuestionDaoImpl questionDao;
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
        assertThat(questionDao).isNotNull();
    }
    @DisplayName("Проверка получения всех вопросов")
    @Test
    public void whenFindAllQuestion_thenReturnAllQuestions(){
        List<Question> questions = questionDao.findAll();
        assertAll(
                () -> assertThat(questions).isNotNull(),
                () -> assertThat(questions).isNotEmpty(),
                () -> assertThat(questions).hasSize(3)
        );
    }
    @DisplayName("Проверка получения конкретного вопроса")
    @Test
    public void whenGetQuestion_thenReturnQuestion(){
        Question question = questionDao.get("q2");
        assertAll(
                () -> assertThat(question).isNotNull(),
                () -> assertThat(question).isEqualTo(new Question("q2", "a2")),
                () -> assertThat(question.getQuestion()).isEqualTo("q2")
        );
    }
    @DisplayName("Проверка выброса исключения если вопрос не обнаружен")
    @Test
    public void whenGetQuestion_thenThrowsException(){
        ServiceException exception = assertThrows(ServiceException.class, () -> questionDao.get("q4"));
        assertThat(exception.getMessage()).isEqualTo(QUESTION_NOT_FOUND);
    }

    @DisplayName("Проверка удаления конкретного вопроса")
    @Test
    public void whenDeleteQuestion_thenQuestionsSizeSmaller(){
        int baforeRemoveSize = questionDao.findAll().size();
        Question removed = new Question("q2", "a2");
        questionDao.remove(removed);
        List<Question> afterRemove = questionDao.findAll();
        assertAll(
                () -> assertThat(afterRemove.size()).isEqualTo(baforeRemoveSize - 1),
                () -> assertThat(afterRemove).doesNotContain(removed)
        );
    }
    @DisplayName("Проверка выброса исключения если удаляемый вопрос не обнаружен")
    @Test
    public void wheDeleteQuestion_thenThrowsException(){
        ServiceException exception = assertThrows(ServiceException.class, () -> questionDao.remove(new Question("q4", "a4")));
        assertThat(exception.getMessage()).isEqualTo(QUESTION_NOT_FOUND);
    }
    @DisplayName("Проверка добавления нового вопроса")
    @Test
    public void whenSaveQuestion_thenQuestionsSizeBiger(){
        int baforeSaveeSize = questionDao.findAll().size();
        Question added = new Question("q4", "a4");
        questionDao.save(added);
        List<Question> afterSave = questionDao.findAll();
        assertAll(
                () -> assertThat(afterSave.size()).isEqualTo(baforeSaveeSize + 1),
                () -> assertThat(afterSave).contains(added)
        );
    }

    @DisplayName("Проверка изменение  вопроса")
    @Test
    public void whenSaveExistQuestion_thenQuestionsSizeNotChanged(){
        int baforeSaveeSize = questionDao.findAll().size();
        Question added = new Question("q2", "a4");
        questionDao.save(added);
        List<Question> afterSave = questionDao.findAll();
        assertAll(
                () -> assertThat(afterSave.size()).isEqualTo(baforeSaveeSize),
                () -> assertThat(afterSave).contains(added)
        );
    }

}