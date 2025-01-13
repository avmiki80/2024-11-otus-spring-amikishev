package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ResourceLoaderConfigTest;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.otus.spring.message.ServiceMessage.PROBLEM_RESOURCE;

@ContextConfiguration(classes = ResourceLoaderConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс ResourceLoaderImpl")
class ResourceLoaderImplTest {
    @Autowired
    private ResourceLoader<Question> correctResourceLoader;
    @Autowired
    private ResourceLoader<Question> wrongResourceLoader;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertAll(
                () -> assertThat(correctResourceLoader).isNotNull(),
                () -> assertThat(wrongResourceLoader).isNotNull()
        );
    }
    @DisplayName("Проверка корректности прочитанных вопросов")
    @Test
    public void wnenCorrectResourceAnotherVersion_thenHasFiveQuestion(){
        List<Question> questions = correctResourceLoader.load();
        assertThat(questions).isNotNull().isNotEmpty().hasSize(5);
    }
    @DisplayName("Проверка выброса исключения при не корректном пути к csv файлу")
    @Test
    public void wnenWrongResource_thenDoesThrowException(){
        assertAll(
                () -> {
                    ServiceException exception = assertThrows(ServiceException.class, () -> wrongResourceLoader.load());
                    assertThat(exception.getMessage()).isEqualTo(PROBLEM_RESOURCE);
                }
        );
    }
}