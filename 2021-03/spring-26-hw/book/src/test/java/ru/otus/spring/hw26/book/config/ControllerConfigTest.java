package ru.otus.spring.hw26.book.config;


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
import ru.otus.spring.hw26.book.controller.*;
import ru.otus.spring.hw26.book.dto.AuthorDto;
import ru.otus.spring.hw26.book.dto.BookDto;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.dto.GenreDto;
import ru.otus.spring.hw26.book.search.AuthorSearch;
import ru.otus.spring.hw26.book.search.BookSearch;
import ru.otus.spring.hw26.book.search.CommentSearch;
import ru.otus.spring.hw26.book.search.GenreSearch;
import ru.otus.spring.hw26.book.service.CrudService;
import ru.otus.spring.hw26.book.service.mass.MassMethodService;

import javax.servlet.Filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@TestConfiguration
@ComponentScan(
        basePackages = "ru.otus.spring.hw26.book.config",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {ConfigTest.class, RestTemplateConfig.class, AsyncConfig.class, WebConfig.class})
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
    @MockBean
    private MassMethodService<CommentDto> massCreateComment;

    @SneakyThrows
    @Bean
    public MockMvc mvc() {
        StandaloneMockMvcBuilder mockMvcBuilder = MockMvcBuilders.standaloneSetup(
                new AuthorController(authorService),
                new BookController(bookService),
                new CommentController(commentService, massCreateComment),
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
