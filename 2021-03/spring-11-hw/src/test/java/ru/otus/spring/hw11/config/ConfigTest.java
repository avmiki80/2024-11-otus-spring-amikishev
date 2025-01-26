package ru.otus.spring.hw11.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.hw11.domain.Author;
import ru.otus.spring.hw11.domain.Book;
import ru.otus.spring.hw11.domain.Comment;
import ru.otus.spring.hw11.domain.Genre;
import ru.otus.spring.hw11.dto.AuthorDto;
import ru.otus.spring.hw11.dto.BookDto;
import ru.otus.spring.hw11.dto.CommentDto;
import ru.otus.spring.hw11.dto.GenreDto;
import ru.otus.spring.hw11.mapper.*;
import ru.otus.spring.hw11.repository.*;
import ru.otus.spring.hw11.search.AuthorSearch;
import ru.otus.spring.hw11.search.BookSearch;
import ru.otus.spring.hw11.search.CommentSearch;
import ru.otus.spring.hw11.search.GenreSearch;
import ru.otus.spring.hw11.service.*;

@TestConfiguration
@Import({GenreRepository.class, AuthorRepository.class, BookRepository.class, CommentRepository.class})
public class ConfigTest {
    @Autowired
    private CommonRepository<Genre, GenreSearch> genreRepository;
    @Autowired
    private CommonRepository<Author, AuthorSearch> authorRepository;
    @Autowired
    private CustomRepository<Genre, GenreSearch> customGenreRepository;
    @Autowired
    private CustomRepository<Author, AuthorSearch> customAuthorRepository;
    @Autowired
    private CommonRepository<Book, BookSearch> bookRepository;
    @Autowired
    private CommonRepository<Comment, CommentSearch> commentRepository;
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
        return new BookService(bookRepository, customGenreRepository, customAuthorRepository, commentRepository, bookMapper());
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
