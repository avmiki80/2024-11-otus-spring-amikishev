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
import ru.otus.spring.data.BookDataBuilder;
import ru.otus.spring.data.GenreDataBuilder;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.ServiceException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
//@Transactional(propagation = Propagation.NOT_SUPPORTED)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookService для работы с книгами")
class BookServiceTest {
    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final long EXISTING_BOOK_ID = 1L;
//    private static final long SAVED_EXPECTED_BOOK_ID = 3L;
    @Autowired
    private BookService bookService;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(bookService).isNotNull();
    }
    @DisplayName("Получение всех книг")
    @Test
    public void shouldReturnAllGenres(){
        Book expectedBook = BookDataBuilder.book().build();
        List<Book> books = bookService.findAll();
        assertAll(
                () -> assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT),
                () -> assertThat(books).usingFieldByFieldElementComparator()
                        .contains(expectedBook)
        );
    }

    @DisplayName("Сохрание книги")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenSaveBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withTitle("Test").withId(null).build();
        long bookId = bookService.insert(expectedBook);
        Book actualBook = bookService.findById(bookId);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }
    @DisplayName("Сохрание книги с новым жанром")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenSaveBookWithNewGenre_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withTitle("Test")
                .withId(null)
                .withGenre(GenreDataBuilder.genre().withTitle("Test").build())
                .build();
        long bookId = bookService.insert(expectedBook);
        Book actualBook = bookService.findById(bookId);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringFields("genreId").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Сохрание книги с новым автором")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenSaveBookWithNewAuthor_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withTitle("Test")
                .withId(null)
                .withAuthor(AuthorDataBuilder.author().withLastname("Test").build())
                .build();
        long bookId = bookService.insert(expectedBook);
        Book actualBook = bookService.findById(bookId);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringFields("authorId").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Изменение книги")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenUpdateBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withTitle("Test").build();
        bookService.update(expectedBook);
        Book actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Получение книги по ID")
    @Test
    public void whenGetBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withId(EXISTING_BOOK_ID).build();
        Book actualBook = bookService.findById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }
    @DisplayName("Удаление книги по ID")
    @Test
//    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void whenDeleteGenre_thenThrowsExceptionIfGetBookAgain(){
        assertThatCode(() -> bookService.findById(EXISTING_BOOK_ID)).doesNotThrowAnyException();
        bookService.deleteById(EXISTING_BOOK_ID);
        assertThatThrownBy(() -> bookService.findById(EXISTING_BOOK_ID)).isInstanceOf(ServiceException.class);
    }
}