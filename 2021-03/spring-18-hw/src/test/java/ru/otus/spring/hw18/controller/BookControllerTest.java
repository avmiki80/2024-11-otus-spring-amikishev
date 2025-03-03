package ru.otus.spring.hw18.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.hw18.config.ControllerConfigTest;
import ru.otus.spring.hw18.config.CustomSecurityConfig;
import ru.otus.spring.hw18.data.BookDTODataBuilder;
import ru.otus.spring.hw18.dto.BookDto;
import ru.otus.spring.hw18.exception.ServiceException;
import ru.otus.spring.hw18.search.BookSearch;
import ru.otus.spring.hw18.service.CrudService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = ControllerConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс BookController REST контроллер для работы с книгами")
class BookControllerTest {
    private static final String TITLE = "some title";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CrudService<BookDto, BookSearch> bookService;
    private static final String USER_ROLE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJ1c2VyIiwicm9sZXMiOlsiVVNFUiJdLCJleHQiOjIwMTYyMzkwMjIsImlhdCI6MTUxNjIzOTAyMn0.44ThbDgKhL8fWzWAme7PPR6iMzLS0SB3NxeMp3Jb3_X5ApKLuHpxd609JX_8Ca7ZwL_Tzd4CyGX_5LV8_id8Hw";
    private static final String ADMIN_ROLE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImV4dCI6MjAxNjIzOTAyMiwiaWF0IjoxNTE2MjM5MDIyfQ.QqUWw8a3AbmX6lRg7xl2Ks3WMk7ORurS-M-qjiiTTdDB-zOK7_-RRTavgPBXNHjq5jVsC6U29fY6RRFh3YP3SA";
    private static final String WRONG_SIGNATURE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIiwiVVNFUiJdLCJleHQiOjIwMTYyMzkwMjIsImlhdCI6MTUxNjIzOTAyMn0.RMnV8tjWp73JARo_1Q8nqYsxqrKBS5qW3M1Ks2qrlSKUEAxP_XSeeO3xd-__5Z614PaMgQJacvVijJVJRQq8Dg";

    private static final String FOO_ROLE_JWT_TOKEN = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJ1c2VyIiwicm9sZXMiOlsiRk9PIl0sImV4dCI6MjAxNjIzOTAyMiwiaWF0IjoxNTE2MjM5MDIyfQ.b3WQl13-tv7XuNeJFRKPGzWxRFXCgGbnWBLJiqlnoWkfC7VOnCIAGofzdno4slOP6WmIqQNS6wQzsHo8kIQ1ZQ";
    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка настройки AccessDecisionManager, так как задан токен с ролью ADMIN, " +
            "то PreInvocationAuthorizationAdviceVoter должен проголосовать за предоставление доступа, " +
            "не смотря на заданую роль USER")
    void checkPreAuthorizeVoterForSaveNewBook() throws Exception {
        BookDto book = BookDTODataBuilder.book().withId(null).build();
        String forSave = mapper.writeValueAsString(book);

        BookDto expectedBook = BookDTODataBuilder.book().build();
        String expectedResult = mapper.writeValueAsString(expectedBook);

        given(bookService.save(book)).willReturn(expectedBook);

        mvc.perform(post("/book")
                        .header(
                                "Authorization",
                                "Bearer " + ADMIN_ROLE_JWT_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }
    @WithMockUser(
            roles = "USER"
    )
    @DisplayName("Проверка запрета доступа на запись книги для роли USER")
    @Test
    void whenSaveWithUserRoleAndUserToken_thenReturnStatusForbidden() throws Exception {
        BookDto book = BookDTODataBuilder.book().withId(null).build();
        String forSave = mapper.writeValueAsString(book);
        mvc.perform(post("/book")
                        .header(
                                "Authorization",
                                "Bearer " + USER_ROLE_JWT_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка запрета доступа на изменение книги для роли USER")
    void whenUpdateWithWUserRoleAndUserToken_thenReturnStatusForbidden() throws Exception {
        BookDto book = BookDTODataBuilder.book().build();
        String forSave = mapper.writeValueAsString(book);
        mvc.perform(put("/book/{id}", 1)
                        .header(
                                "Authorization",
                                "Bearer " + USER_ROLE_JWT_TOKEN)
                        .contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка запрета доступа на поиск книг если токен имеет не корректную подпись")
    void whenFindWithParamsWithUserRoleAndWrongToken_thenReturnStatusUnauthorized() throws Exception {
        mvc.perform(get("/book")
                        .header(
                                "Authorization",
                                "Bearer " + WRONG_SIGNATURE_JWT_TOKEN)
                        .param("title", TITLE))
                .andExpect(status().isUnauthorized());

    }

    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка запрета доступа на удаление книги для роли USER")
    void whenDeleteWithUserRoleAndUserToken_thenReturnStatusForbidden() throws Exception {
        mvc.perform(delete("/book/1")
                        .header(
                            "Authorization",
                            "Bearer " + USER_ROLE_JWT_TOKEN)
                )
                .andExpect(status().isForbidden());
    }
    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка разрешения доступа на получение книги по ИД если токен для роли USER")
    void whenFindByIdWithUserRoleAndUserToken_thenReturnStatusOk() throws Exception {
        mvc.perform(get("/book/1")
                        .header(
                                "Authorization",
                                "Bearer " + USER_ROLE_JWT_TOKEN))
                .andExpect(status().isOk());
    }
    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка разрешения доступа на получение книги по ИД если токен для роли FOO")
    void whenFindByIdWithUserRoleAndFooToken_thenReturnStatusForbidden() throws Exception {
        mvc.perform(get("/book/1")
                        .header(
                                "Authorization",
                                "Bearer " + FOO_ROLE_JWT_TOKEN))
                .andExpect(status().isForbidden());
    }
    @WithMockUser(
            roles = "USER"
    )
    @Test
    @DisplayName("Проверка запрета доступа на получение книги по ИД если токен имеет не корректную подпись")
    void whenFindByIdWithUserRoleAndWrongToken_thenReturnStatusUnauthorized() throws Exception {
        mvc.perform(get("/book/1")
                        .header(
                                "Authorization",
                                "Bearer " + WRONG_SIGNATURE_JWT_TOKEN))
                .andExpect(status().isUnauthorized());
    }
    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        BookDto book = BookDTODataBuilder.book().withId(null).build();
        String forSave = mapper.writeValueAsString(book);

        BookDto expectedBook = BookDTODataBuilder.book().build();
        String expectedResult = mapper.writeValueAsString(expectedBook);

        given(bookService.save(book)).willReturn(expectedBook);

        mvc.perform(post("/book").contentType(APPLICATION_JSON)
                        .content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectUpdateBookTitle() throws Exception {
        BookDto book = BookDTODataBuilder.book().build();
        String forSave = mapper.writeValueAsString(book);

        BookDto expectedBook = BookDTODataBuilder.book().withTitle("test").build();
        String expectedResult = mapper.writeValueAsString(expectedBook);

        given(bookService.update(book.getId(), book)).willReturn(expectedBook);

        mvc.perform(put("/book/{id}", 1).contentType(APPLICATION_JSON).content(forSave))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedBooks() throws Exception {
        List<BookDto> books = List.of(
                BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build()
        );

        List<BookDto> expectedBooks = List.of(
                BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build()
        );

        given(bookService.findByParams(BookSearch.builder()
                .title(TITLE)
                .build())).willReturn(books);

        mvc.perform(get("/book").param("title", TITLE))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));

    }

    @WithMockUser(
            roles = {"ADMIN"}
    )
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        mvc.perform(delete("/book/1"))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteById(1L);
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnCorrectBookByIdI() throws Exception {
        BookDto expectedBook = BookDTODataBuilder.book().withId(1L).withTitle(TITLE).build();
        given(bookService.findById(expectedBook.getId())).willReturn(expectedBook);

        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @WithMockUser(
            roles = {"ADMIN", "USER"}
    )
    @Test
    void shouldReturnExpectedErrorWhenBooktNotFound() throws Exception {
        given(bookService.findById(1L)).willThrow(new ServiceException("exception.object-not-found"));

        mvc.perform(get("/book/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception.object-not-found"));
    }
}