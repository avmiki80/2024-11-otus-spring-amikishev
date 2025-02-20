package ru.otus.spring.hw15.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.spring.hw15.controller.*;
import ru.otus.spring.hw15.domain.Author;
import ru.otus.spring.hw15.domain.Book;
import ru.otus.spring.hw15.domain.Comment;
import ru.otus.spring.hw15.domain.Genre;
import ru.otus.spring.hw15.dto.AuthorDto;
import ru.otus.spring.hw15.dto.BookDto;
import ru.otus.spring.hw15.dto.CommentDto;
import ru.otus.spring.hw15.dto.GenreDto;
import ru.otus.spring.hw15.mapper.*;
import ru.otus.spring.hw15.repository.*;
import ru.otus.spring.hw15.search.AuthorSearch;
import ru.otus.spring.hw15.search.BookSearch;
import ru.otus.spring.hw15.search.CommentSearch;
import ru.otus.spring.hw15.search.GenreSearch;
import ru.otus.spring.hw15.service.*;

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
    public Mapper<GenreDto, Genre> genreMapper(){
        return new GenreMapper();
    }
    @Bean
    public CrudService<GenreDto, GenreSearch> genreService(){
        return new GenreService(genreRepository, genreMapper());
    }
    @Bean
    public Mapper<AuthorDto, Author> authorMapper(){
        return new AuthorMapper();
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
