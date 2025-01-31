package ru.otus.spring.hw11.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw11.data.BookDataBuilder;
import ru.otus.spring.hw11.domain.Author;
import ru.otus.spring.hw11.domain.Book;
import ru.otus.spring.hw11.domain.Genre;
import ru.otus.spring.hw11.search.BookSearch;
import ru.otus.spring.hw11.config.ConfigTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;
@DataJpaTest
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookRepository для работы с книгами")
@ContextConfiguration(classes = ConfigTest.class)
class BookRepositoryTest {
    private static final int EXPECTED_BOOKS_COUNT = 2;
    private static final long EXISTING_BOOK_ID = 1L;
    private static final String EXISTING_BOOK_TITLE = "Test1";

    @Autowired
    private CommonRepository<Book, BookSearch> bookRepository;
    @Autowired
    private TestEntityManager em;
    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertThat(bookRepository).isNotNull();
    }
    @DisplayName("Получение всех книг")
    @Test
    public void shouldReturnAllBooks(){
        Book expectedBook = BookDataBuilder.book().withId(EXISTING_BOOK_ID).withTitle(EXISTING_BOOK_TITLE).build();
        List<Book> books = bookRepository.findAll();
        assertAll(
                () -> assertThat(books.size()).isEqualTo(EXPECTED_BOOKS_COUNT),
                () -> assertThat(books).usingElementComparatorIgnoringFields("comments").contains(expectedBook)
        );
    }

    @DisplayName("Получение книг с фильтрацией")
    @Test
    public void shouldReturnBooksByFilter(){
        Book expectedBook = BookDataBuilder.book().withId(EXISTING_BOOK_ID).withTitle(EXISTING_BOOK_TITLE).build();
        List<Book> books = bookRepository.findByParams(BookSearch.builder().firstname("алек").build());
        assertAll(
                () -> assertThat(books.size()).isEqualTo(1),
                () -> assertThat(books).usingElementComparatorIgnoringFields("comments").contains(expectedBook)
        );
    }

    @DisplayName("Сохрание книги")
    @Test
    public void whenSaveBook_thenCorrectResult(){        ;
        Book expectedBook = BookDataBuilder.book()
                .withId(null)
                .withGenre(em.find(Genre.class, 1L))
                .withAuthor(em.find(Author.class, 1L))
                .build();
        Book actualBook = bookRepository.save(expectedBook);
        assertAll(
                () -> assertThat(actualBook).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook)
        );
    }

    @DisplayName("Изменение книги")
    @Test
    public void whenUpdateBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book()
                .withTitle("test")
                .withGenre(em.find(Genre.class, 1L))
                .withAuthor(em.find(Author.class, 1L))
                .build();
        Book actualBook = bookRepository.save(expectedBook);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("Получение книги по ID")
    @Test
    public void whenGetBook_thenCorrectResult(){
        Book expectedBook = BookDataBuilder.book()
                .withId(EXISTING_BOOK_ID)
                .withTitle(EXISTING_BOOK_TITLE)
                .withGenre(em.find(Genre.class, 1L))
                .withAuthor(em.find(Author.class, 1L))
                .build();
        Book actualBook = bookRepository.findById(expectedBook.getId()).get();
        assertThat(actualBook).usingRecursiveComparison().ignoringFields("comments").isEqualTo(expectedBook);
    }
    @DisplayName("Удаление книги по ID")
    @Test
    public void whenDeleteBook_thenThrowsExceptionIfGetBookAgain(){
        assertThatCode(() -> bookRepository.findById(EXISTING_BOOK_ID)).doesNotThrowAnyException();
        bookRepository.deleteById(EXISTING_BOOK_ID);
        em.clear();
        assertThat(bookRepository.findById(EXISTING_BOOK_ID)).isEqualTo(Optional.empty());
    }
}