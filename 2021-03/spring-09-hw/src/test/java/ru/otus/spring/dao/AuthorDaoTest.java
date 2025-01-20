package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.ConfigTest;
import ru.otus.spring.data.AuthorDataBuilder;
import ru.otus.spring.domain.Author;
import ru.otus.spring.search.AuthorSearch;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс AuthorDao для работы с авторами")
class AuthorDaoTest {
    private static final int EXPECTED_AUTHORS_COUNT = 2;
    private static final long EXISTING_AUTHOR_ID = 1L;
    private static final String EXISTING_AUTHOR_LASTNAME = "Пушкин";
    private static final String EXISTING_AUTHOR_FIRSTNAME = "Александр";
//    private static final long SAVED_EXPECTED_AUTHOR_ID = 3L;
    @Autowired
    private Dao<Author, AuthorSearch> authorDao;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(authorDao).isNotNull();
    }
    @DisplayName("Получение всех авторов")
    @Test
    public void shouldReturnAllAuthors(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID).withFirstname(EXISTING_AUTHOR_FIRSTNAME).withLastname(EXISTING_AUTHOR_LASTNAME).build();
        List<Author> authors = authorDao.findAll();
        assertAll(
                () -> assertThat(authors.size()).isEqualTo(EXPECTED_AUTHORS_COUNT),
                () -> assertThat(authors).usingFieldByFieldElementComparator()
                        .contains(expectedAuthor)
        );
    }

    @DisplayName("Сохрание автора")
    @Test
    public void whenSaveAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(null).build();
        long authorId = authorDao.insert(expectedAuthor);
        Author actualAuthor = authorDao.findById(authorId);
        assertAll(
                () -> assertThat(actualAuthor).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedAuthor)
        );
    }

    @DisplayName("Изменение автора")
    @Test
    public void whenUpdateAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().build();
        authorDao.update(expectedAuthor);
        Author actualAuthor = authorDao.findById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("Получение автора по ID")
    @Test
    public void whenGetAuthor_thenCorrectResult(){
        Author expectedAuthor = AuthorDataBuilder.author().withId(EXISTING_AUTHOR_ID)
                .withFirstname(EXISTING_AUTHOR_FIRSTNAME)
                .withLastname(EXISTING_AUTHOR_LASTNAME).build();
        Author actualAuthor = authorDao.findById(expectedAuthor.getId());
        assertThat(actualAuthor).usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
    @DisplayName("Удаление автора по ID")
    @Test
    public void whenDeleteAuthor_thenThrowsExceptionIfGetAuthorAgain(){
        assertThatCode(() -> authorDao.findById(EXISTING_AUTHOR_ID)).doesNotThrowAnyException();
        authorDao.deleteById(EXISTING_AUTHOR_ID);
        assertThatThrownBy(() -> authorDao.findById(EXISTING_AUTHOR_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}