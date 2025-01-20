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
import ru.otus.spring.data.GenreDataBuilder;
import ru.otus.spring.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс GenreDao для работы с жанрами")
class GenreDaoTest {
    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long EXISTING_GENRE_ID = 1L;
    private static final String EXISTING_GENRE_TITLE = "Фантастика";
//    private static final long SAVED_EXPECTED_GENRE_ID = 3L;
    @Autowired
    private Dao<Genre> genreDao;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(genreDao).isNotNull();
    }
    @DisplayName("Получение всех жанров")
    @Test
    public void shouldReturnAllGenres(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        List<Genre> genres = genreDao.findAll();
        assertAll(
                () -> assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT),
                () -> assertThat(genres).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }

    @DisplayName("Сохрание жанра")
    @Test
    public void whenSaveGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(null).build();
        long genreId = genreDao.insert(expectedGenre);
        Genre actualGenre = genreDao.findById(genreId);
        assertAll(
                () -> assertThat(actualGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGenre)
        );
    }

    @DisplayName("Изменение жанра")
    @Test
    public void whenUpdateGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().build();
        genreDao.update(expectedGenre);
        Genre actualGenre = genreDao.findById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Получение жанра")
    @Test
    public void whenGetGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        Genre actualGenre = genreDao.findById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Удаление жанра по ID")
    @Test
    public void whenDeleteGenre_thenThrowsExceptionIfGetGenreAgain(){
        assertThatCode(() -> genreDao.findById(EXISTING_GENRE_ID)).doesNotThrowAnyException();
        genreDao.deleteById(EXISTING_GENRE_ID);
        assertThatThrownBy(() -> genreDao.findById(EXISTING_GENRE_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}