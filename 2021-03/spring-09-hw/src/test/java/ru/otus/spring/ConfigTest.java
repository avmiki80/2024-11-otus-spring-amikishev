package ru.otus.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.BookDao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@TestConfiguration
public class ConfigTest {
    @Autowired
    private NamedParameterJdbcOperations jdbc;
    @Bean
    public GenreDao genreDao(){
        return new GenreDao(jdbc);
    }

    @Bean
    public GenreService genreService(){
        return new GenreService(genreDao());
    }
    @Bean
    public AuthorDao authorDao(){
        return new AuthorDao(jdbc);
    }
    @Bean
    public AuthorService authorService(){
        return new AuthorService(authorDao());
    }
    @Bean
    public BookDao bookDao(){
        return new BookDao(jdbc);
    }
    @Bean
    public BookService bookService(){
        return new BookService(bookDao(), genreService(), authorService());
    }
}
