package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.ConfigTest;
import ru.otus.spring.data.GenreDataBuilder;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.ServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@DisplayName("Класс GenreService для работы с жанрами")
class GenreServiceTest {
    private static final int EXPECTED_GENRES_COUNT = 2;
    private static final long EXISTING_GENRE_ID = 1L;
    private static final String EXISTING_GENRE_TITLE = "Фантастика";
//    private static final long SAVED_EXPECTED_GENRE_ID = 3L;
    private static final long UNEXISTING_GENRE_ID = 3000L;

    @Autowired
    private GenreService genreService;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(genreService).isNotNull();
    }
    @DisplayName("Получение всех жанров")
    @Test
    public void shouldReturnAllGenres(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        List<Genre> genres = genreService.findAll();
        assertAll(
                () -> assertThat(genres.size()).isEqualTo(EXPECTED_GENRES_COUNT),
                () -> assertThat(genres).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }
    @DisplayName("Поиск по заголовку жанра")
    @Test
    public void whenFindByTitle_shouldReturnFilteredGenres(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        assertAll(
                () -> assertDoesNotThrow(() -> genreService.findByTitle(EXISTING_GENRE_TITLE)),
                () -> assertThat(genreService.findByTitle(EXISTING_GENRE_TITLE)).usingFieldByFieldElementComparator()
                        .contains(expectedGenre)
        );
    }

    @DisplayName("Сохрание жанра")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenSaveGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(null).build();
        long genreId = genreService.insert(expectedGenre);
        Genre actualGenre = genreService.findById(genreId);
        assertAll(
                () -> assertThat(actualGenre).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedGenre)
        );
    }

    @DisplayName("Изменение жанра")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenUpdateGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().build();
        genreService.update(expectedGenre);
        Genre actualGenre = genreService.findById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("Получение жанра")
    @Test
    public void whenGetGenre_thenCorrectResult(){
        Genre expectedGenre = GenreDataBuilder.genre().withId(EXISTING_GENRE_ID).withTitle(EXISTING_GENRE_TITLE).build();
        Genre actualGenre = genreService.findById(expectedGenre.getId());
        assertThat(actualGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }
    @DisplayName("Получение жанра по не существующему ID")
    @Test
    public void whenGetGenreWithUnexistedId_thenThrowsException(){
        assertThatThrownBy(() -> genreService.findById(UNEXISTING_GENRE_ID)).isInstanceOf(ServiceException.class);
    }
    @DisplayName("Удаление жанра по ID")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenDeleteGenre_thenThrowsExceptionIfGetGenreAgain(){
        assertThatCode(() -> genreService.findById(EXISTING_GENRE_ID)).doesNotThrowAnyException();
        genreService.deleteById(EXISTING_GENRE_ID);
        assertThatThrownBy(() -> genreService.findById(EXISTING_GENRE_ID)).isInstanceOf(ServiceException.class);
    }
}