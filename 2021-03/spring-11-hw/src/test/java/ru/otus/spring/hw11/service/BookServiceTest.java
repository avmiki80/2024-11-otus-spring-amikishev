package ru.otus.spring.hw11.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw11.config.ConfigTest;
import ru.otus.spring.hw11.data.BookDTODataBuilder;
import ru.otus.spring.hw11.dto.BookDto;
import ru.otus.spring.hw11.exception.ServiceException;
import ru.otus.spring.hw11.search.BookSearch;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@DataJpaTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookService для работы с книгами")
class BookServiceTest {
    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final long EXISTING_BOOK_ID = 1L;
    private static final String EXISTING_BOOK_TITLE = "Test1";
    @Autowired
    private BookService bookService;
    @Autowired
    private TestEntityManager em;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(bookService).isNotNull();
    }
    @DisplayName("Получение всех книг")
    @Test
    public void shouldReturnAllBooks(){
        BookDto expectedBook = BookDTODataBuilder.book().build();
        List<BookDto> books = bookService.findAll();
        assertAll(
                () -> assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT),
                () -> assertThat(books).usingFieldByFieldElementComparator()
                        .contains(expectedBook)
        );
    }
    @DisplayName("Получение книг с фильтрацией")
    @Test
    public void shouldReturnBooksByFilter(){
        BookDto expectedBook = BookDTODataBuilder.book().withId(EXISTING_BOOK_ID).withTitle(EXISTING_BOOK_TITLE).build();
        List<BookDto> books = bookService.findByParams(BookSearch.builder().firstname("алек").build());
        assertAll(
                () -> assertThat(books.size()).isEqualTo(1),
                () -> assertThat(books).usingElementComparatorIgnoringFields("comments").contains(expectedBook)
        );
    }
    @DisplayName("Сохрание книги")
    @Test
    public void whenSaveBook_thenCorrectResult(){
        BookDto expectedBook = BookDTODataBuilder.book().withTitle("Test").build();
        BookDto actualBook = bookService.save(expectedBook);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }
    @DisplayName("Сохрание книги с новым жанром")
    @Test
    public void whenSaveBookWithNewGenre_thenCorrectResult(){
        BookDto expectedBook = BookDTODataBuilder.book().withTitle("Test")
                .withId(null)
                .withGenre("Test")
                .build();
        BookDto actualBook = bookService.save(expectedBook);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Сохрание книги с новым автором")
    @Test
    public void whenSaveBookWithNewAuthor_thenCorrectResult(){
        BookDto expectedBook = BookDTODataBuilder.book().withTitle("Test")
                .withId(null)
                .withFirstname("Test")
                .build();
        BookDto actualBook = bookService.save(expectedBook);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Изменение книги")
    @Test
    public void whenUpdateBook_thenCorrectResult(){
        BookDto expectedBook = BookDTODataBuilder.book().withTitle("Test").build();
        BookDto actualBook = bookService.save(expectedBook);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Получение книги по ID")
    @Test
    public void whenGetBook_thenCorrectResult(){
        BookDto expectedBook = BookDTODataBuilder.book().withId(EXISTING_BOOK_ID).build();
        BookDto actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }
    @DisplayName("Удаление книги по ID")
    @Test
    public void whenDeleteGenre_thenThrowsExceptionIfGetBookAgain(){
        assertThatCode(() -> bookService.findById(EXISTING_BOOK_ID)).doesNotThrowAnyException();
        bookService.deleteById(EXISTING_BOOK_ID);
        em.clear();
        assertThatThrownBy(() -> bookService.findById(EXISTING_BOOK_ID)).isInstanceOf(ServiceException.class);
    }
}