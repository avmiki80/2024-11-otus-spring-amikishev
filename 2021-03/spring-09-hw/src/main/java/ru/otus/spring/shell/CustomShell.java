package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.service.AuthorService;
import ru.otus.spring.service.BookService;
import ru.otus.spring.service.GenreService;

@ShellComponent
@RequiredArgsConstructor
public class CustomShell {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookService bookService;

    @ShellMethod(value = "Add book", key = {"ab"})
    public void saveBook(
            @ShellOption(defaultValue = "Book title") String title,
            @ShellOption(defaultValue = "Book genre") String genre,
            @ShellOption(defaultValue = "Author firstname") String firstname,
            @ShellOption(defaultValue = "Author lastname") String lastname
    ) {
        bookService.insert(Book.builder().title(title).genre(new Genre(null, genre)).author(new Author(null, firstname, lastname)).build());
    }
    @ShellMethod(value = "Update book", key = {"ub"})
    public void updateBook(
            @ShellOption(defaultValue = "Book id") String id,
            @ShellOption(defaultValue = "Book title") String title,
            @ShellOption(defaultValue = "Book genre") String genre,
            @ShellOption(defaultValue = "Author firstname") String firstname,
            @ShellOption(defaultValue = "Author lastname") String lastname
    ) {
        bookService.update(Book.builder().id(Long.parseLong(id)).title(title).genre(new Genre(null, genre)).author(new Author(null, firstname, lastname)).build());
    }
    @ShellMethod(value = "Delete book", key = {"db"})
    public void deleteBook(
            @ShellOption(defaultValue = "ID Book") String id
    ) {
        bookService.deleteById(Long.parseLong(id));
        String.format("success delete %s", id);
    }
    @ShellMethod(value = "Get book", key = {"gb"})
    public void getBook(
            @ShellOption(defaultValue = "ID Book") String id
    ) {
        System.out.println(bookService.findById(Long.parseLong(id)));
    }
    @ShellMethod(value = "Find books", key = {"fb"})
    public void findBooks(
    ) {
        bookService.findAll().forEach(i -> System.out.println(i.toString()));
    }

}
