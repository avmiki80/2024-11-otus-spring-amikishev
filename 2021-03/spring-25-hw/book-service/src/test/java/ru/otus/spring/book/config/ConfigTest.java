package ru.otus.spring.book.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.book.domain.Author;
import ru.otus.spring.book.domain.Book;
import ru.otus.spring.book.domain.Comment;
import ru.otus.spring.book.domain.Genre;
import ru.otus.spring.book.dto.AuthorDto;
import ru.otus.spring.book.dto.BookDto;
import ru.otus.spring.book.dto.CommentDto;
import ru.otus.spring.book.dto.GenreDto;
import ru.otus.spring.book.mapper.*;
import ru.otus.spring.book.repository.AuthorRepository;
import ru.otus.spring.book.repository.BookRepository;
import ru.otus.spring.book.repository.CommentRepository;
import ru.otus.spring.book.repository.GenreRepository;
import ru.otus.spring.book.search.AuthorSearch;
import ru.otus.spring.book.search.BookSearch;
import ru.otus.spring.book.search.CommentSearch;
import ru.otus.spring.book.search.GenreSearch;
import ru.otus.spring.book.service.*;

@TestConfiguration
public class ConfigTest {
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CommentRepository commentRepository;
    @MockBean
    private ModeratorService moderatorService;
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
    @Bean
    public Mapper<GenreDto, Genre> genreMapper(){
        return new GenreMapper(objectMapper());
    }
    @Bean
    public CrudService<GenreDto, GenreSearch> genreService(){
        return new GenreService(genreRepository, genreMapper());
    }
    @Bean
    public Mapper<AuthorDto, Author> authorMapper(){
        return new AuthorMapper(objectMapper());
    }
    @Bean
    public CrudService<AuthorDto, AuthorSearch> authorService(){
        return new AuthorService(authorRepository, authorMapper());
    }
    @Bean
    public Mapper<BookDto, Book> bookMapper(){
        return new BookMapper();
    }

    @Bean
    public CrudService<BookDto, BookSearch> bookService(){
        return new BookService(bookRepository, genreRepository, authorRepository, bookMapper());
    }
    @Bean
    public Mapper<CommentDto, Comment> commentMapper(){
        return new CommentMapper();
    }
    @Bean
    public CrudService<CommentDto, CommentSearch> commentService(){
        return new CommentService(commentRepository, bookRepository, commentMapper(), moderatorService);
    }
}
