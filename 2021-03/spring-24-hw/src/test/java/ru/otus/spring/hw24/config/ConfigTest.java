package ru.otus.spring.hw24.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.hw24.domain.Author;
import ru.otus.spring.hw24.domain.Book;
import ru.otus.spring.hw24.domain.Comment;
import ru.otus.spring.hw24.domain.Genre;
import ru.otus.spring.hw24.dto.AuthorDto;
import ru.otus.spring.hw24.dto.BookDto;
import ru.otus.spring.hw24.dto.CommentDto;
import ru.otus.spring.hw24.dto.GenreDto;
import ru.otus.spring.hw24.mapper.*;
import ru.otus.spring.hw24.repository.AuthorRepository;
import ru.otus.spring.hw24.repository.BookRepository;
import ru.otus.spring.hw24.repository.CommentRepository;
import ru.otus.spring.hw24.repository.GenreRepository;
import ru.otus.spring.hw24.search.AuthorSearch;
import ru.otus.spring.hw24.search.BookSearch;
import ru.otus.spring.hw24.search.CommentSearch;
import ru.otus.spring.hw24.search.GenreSearch;
import ru.otus.spring.hw24.service.*;

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
        return new CommentService(commentRepository, bookRepository, commentMapper());
    }
}
