package ru.otus.spring.hw15.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.spring.hw15.controller.*;
import ru.otus.spring.hw15.dto.AuthorDto;
import ru.otus.spring.hw15.dto.BookDto;
import ru.otus.spring.hw15.dto.CommentDto;
import ru.otus.spring.hw15.dto.GenreDto;
import ru.otus.spring.hw15.search.AuthorSearch;
import ru.otus.spring.hw15.search.BookSearch;
import ru.otus.spring.hw15.search.CommentSearch;
import ru.otus.spring.hw15.search.GenreSearch;
import ru.otus.spring.hw15.service.CrudService;

@TestConfiguration
public class ControllerConfigTest {
    @MockBean
    private CrudService<AuthorDto, AuthorSearch> authorService;
    @MockBean
    private CrudService<BookDto, BookSearch> bookService;
    @MockBean
    private CrudService<CommentDto, CommentSearch> commentService;
    @MockBean
    private CrudService<GenreDto, GenreSearch> genreService;

    @Bean
    public MockMvc mvc(){
        return MockMvcBuilders.standaloneSetup(
                new AuthorController(authorService),
                new BookController(bookService),
                new CommentController(commentService),
                new GenreController(genreService),
                new ExceptionHandlerController()).build();
    }
    @Bean
    public ObjectMapper mapper(){
        return new ObjectMapper();
    }
}
