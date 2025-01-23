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
import ru.otus.spring.data.BookDataBuilder;
import ru.otus.spring.domain.Book;
import ru.otus.spring.search.BookSearch;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
@JdbcTest
@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookDao для работы с книгами")
class BookDaoTest {
    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final long EXISTING_BOOK_ID = 1L;
    private static final String EXISTING_BOOK_TITLE = "Test1";
//    private static final long SAVED_EXPECTED_BOOK_ID = 3L;
    @Autowired
    private Dao<Book, BookSearch> bookDao;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(bookDao).isNotNull();
    }
    @DisplayName("Получение всех книг")
    @Test
    public void shouldReturnAllGenres(){
        Book expectedBook = BookDataBuilder.book().withId(EXISTING_BOOK_ID).withTitle(EXISTING_BOOK_TITLE).build();
        List<Book> books = bookDao.findAll();
        assertAll(
                () -> assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT),
                () -> assertThat(books).usingFieldByFieldElementComparator()
                        .contains(expectedBook)
        );
    }

    @DisplayName("Сохрание книги")
    @Test
    public void whenSaveBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withId(null).build();
        long bookId = bookDao.insert(expectedBook);
        Book actualBook = bookDao.findById(bookId);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Изменение книги")
    @Test
    public void whenUpdateBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().build();
        bookDao.update(expectedBook);
        Book actualBook = bookDao.findById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Получение книги по ID")
    @Test
    public void whenGetBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book().withId(EXISTING_BOOK_ID).withTitle(EXISTING_BOOK_TITLE).build();
        Book actualBook = bookDao.findById(expectedBook.getId());
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }
    @DisplayName("Удаление книги по ID")
    @Test
    public void whenDeleteGenre_thenThrowsExceptionIfGetBookAgain(){
        assertThatCode(() -> bookDao.findById(EXISTING_BOOK_ID)).doesNotThrowAnyException();
        bookDao.deleteById(EXISTING_BOOK_ID);
        assertThatThrownBy(() -> bookDao.findById(EXISTING_BOOK_ID)).isInstanceOf(EmptyResultDataAccessException.class);
    }
}