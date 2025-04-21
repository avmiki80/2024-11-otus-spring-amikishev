package ru.otus.spring.book.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import ru.otus.spring.book.controller.*;
import ru.otus.spring.book.dto.AuthorDto;
import ru.otus.spring.book.dto.BookDto;
import ru.otus.spring.book.dto.CommentDto;
import ru.otus.spring.book.dto.GenreDto;
import ru.otus.spring.book.search.AuthorSearch;
import ru.otus.spring.book.search.BookSearch;
import ru.otus.spring.book.search.CommentSearch;
import ru.otus.spring.book.search.GenreSearch;
import ru.otus.spring.book.service.CrudService;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@TestConfiguration
@ComponentScan(
        basePackages = "ru.otus.spring.book.config",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ConfigTest.class, RestTemplateConfig.class})
)
public class ControllerConfigTest {
    @MockBean
    private CrudService<AuthorDto, AuthorSearch> authorService;
    @MockBean
    private CrudService<BookDto, BookSearch> bookService;
    @MockBean
    private CrudService<CommentDto, CommentSearch> commentService;
    @MockBean
    private CrudService<GenreDto, GenreSearch> genreService;
    @MockBean
    private UserDetailsService userDetailsService;
    @Autowired
    private Filter springSecurityFilterChain;

    @SneakyThrows
    @Bean
    public MockMvc mvc() {
        StandaloneMockMvcBuilder mockMvcBuilder = MockMvcBuilders.standaloneSetup(
                new AuthorController(authorService),
                new BookController(bookService),
                new CommentController(commentService),
                new GenreController(genreService),
                new ExceptionHandlerController());

        mockMvcBuilder.apply(springSecurity(springSecurityFilterChain));
        return mockMvcBuilder.build();
    }


    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

}
