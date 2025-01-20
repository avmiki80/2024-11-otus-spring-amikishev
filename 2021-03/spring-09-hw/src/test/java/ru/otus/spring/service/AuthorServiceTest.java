package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.ConfigTest;
import ru.otus.spring.data.AuthorDataBuilder;
import ru.otus.spring.domain.Author;
import ru.otus.spring.exception.ServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс AuthorService для работы с авторами")
class AuthorServiceTest {
    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final long EXISTING_AUTHOR_ID = 1L;
    private static final long UNEXISTING_AUTHOR_ID = 3000L;
    private static final String EXISTING_AUTHOR_LASTNAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRSTNAME = "Александр";
//    private static final long SAVED_EXPECTED_AUTHOR_ID = 3L;
    @Autowired
    private AuthorService authorService;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(authorService).isNotNull();
    }
    @DisplayName("Получение всех авторов")
    @Test
    public void shouldReturnAllAuthors(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        List<Author> authors = authorService.findAll();
        assertAll(
                () -> assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT),
                () -> assertThat(authors).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Поиск авторов по имени и фамилии")
    @Test
    public void whenFindByLastnameAndFirstname_thenShouldReturnFilteredAuthors(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        assertAll(
                () -> assertDoesNotThrow(() -> authorService.getByFirstnameAndLastname(EXISTING_AUTHOR_FIRSTNAME, EXISTING_AUTHOR_LASTNAME)),
                () -> assertThat(authorService.getByFirstnameAndLastname(EXISTING_AUTHOR_FIRSTNAME, EXISTING_AUTHOR_LASTNAME)).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Сохрание автора")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenSaveAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(null).build();
        long authorId = authorService.insert(expectedAuthor);
        Author actualAuthor = authorService.findById(authorId);
        assertAll(
                () -> assertThat(actualAuthor).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedAuthor)
        );
    }

    @DisplayName("Изменение автора")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenUpdateAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().build();
        authorService.update(expectedAuthor);
        Author actualAuthor = authorService.findById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Получение автора по ID")
    @Test
    public void whenGetAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID)
                .withFirstname(EXISTING_AUTHOR_FIRSTNAME)
                .withLastname(EXISTING_AUTHOR_LASTNAME).build();
        Author actualAuthor = authorService.findById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
    @DisplayName("Получение автора по не существующему ID")
    @Test
    public void whenGetGenreWithUnexistedId_thenThrowsException(){
        assertThatThrownBy(() -> authorService.findById(UNEXISTING_AUTHOR_ID)).isInstanceOf(ServiceException.class);
    }
    @DisplayName("Удаление автора по ID")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenDeleteAuthor_thenThrowsException(){
        assertThatCode(() -> authorService.findById(EXISTING_AUTHOR_ID)).doesNotThrowAnyException();
        authorService.deleteById(EXISTING_AUTHOR_ID);
        assertThatThrownBy(() -> authorService.findById(EXISTING_AUTHOR_ID)).isInstanceOf(ServiceException.class);
    }
}