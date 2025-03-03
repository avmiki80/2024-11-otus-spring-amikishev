package ru.otus.spring.hw18.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.*;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import ru.otus.spring.hw18.controller.*;
import ru.otus.spring.hw18.dto.AuthorDto;
import ru.otus.spring.hw18.dto.BookDto;
import ru.otus.spring.hw18.dto.CommentDto;
import ru.otus.spring.hw18.dto.GenreDto;
import ru.otus.spring.hw18.search.AuthorSearch;
import ru.otus.spring.hw18.search.BookSearch;
import ru.otus.spring.hw18.search.CommentSearch;
import ru.otus.spring.hw18.search.GenreSearch;
import ru.otus.spring.hw18.service.CrudService;

import javax.servlet.Filter;

import java.util.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@TestConfiguration
@ComponentScan(
        basePackages = "ru.otus.spring.hw18.config",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ConfigTest.class})
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
