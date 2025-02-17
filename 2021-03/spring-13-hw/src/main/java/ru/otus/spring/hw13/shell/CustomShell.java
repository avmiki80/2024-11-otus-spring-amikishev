package ru.otus.spring.hw13.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.hw13.dto.BookDto;
import ru.otus.spring.hw13.dto.CommentDto;
import ru.otus.spring.hw13.search.BookSearch;
import ru.otus.spring.hw13.search.CommentSearch;
import ru.otus.spring.hw13.service.CrudService;

@ShellComponent
@RequiredArgsConstructor
public class CustomShell {
    private final CrudService<BookDto, BookSearch> bookService;
    private final CrudService<CommentDto, CommentSearch> commentService;

    @ShellMethod(value = "Add book", key = {"ab"})
    public void saveBook(
            @ShellOption(defaultValue = "Book title") String title,
            @ShellOption(defaultValue = "Book genre") String genre,
            @ShellOption(defaultValue = "Author firstname") String firstname,
            @ShellOption(defaultValue = "Author lastname") String lastname
    ) {
        bookService.save(BookDto.builder().title(title).genre(genre).firstname(firstname).lastname(lastname).build());
    }
    @ShellMethod(value = "Update book", key = {"ub"})
    public void updateBook(
            @ShellOption(defaultValue = "Book id") String id,
            @ShellOption(defaultValue = "Book title") String title,
            @ShellOption(defaultValue = "Book genre") String genre,
            @ShellOption(defaultValue = "Author firstname") String firstname,
            @ShellOption(defaultValue = "Author lastname") String lastname
    ) {
        bookService.update(Long.parseLong(id), BookDto.builder().id(Long.parseLong(id)).title(title).genre(genre).firstname(firstname).lastname(lastname).build());
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

    @ShellMethod(value = "Add comment", key = {"ac"})
    public void saveComment(
            @ShellOption(defaultValue = "Comment text") String text,
            @ShellOption(defaultValue = "Book bookTitle") String bookTitle
    ) {
        commentService.save(CommentDto.builder().text(text).bookTitle(bookTitle).build());
    }
    @ShellMethod(value = "Update comment", key = {"uc"})
    public void updateComment(
            @ShellOption(defaultValue = "Comment id") String id,
            @ShellOption(defaultValue = "Comment text") String text,
            @ShellOption(defaultValue = "Book bookTitle") String bookTitle
    ) {
        commentService.update(Long.parseLong(id), CommentDto.builder().id(Long.parseLong(id)).text(text).bookTitle(bookTitle).build());
    }
    @ShellMethod(value = "Delete comment", key = {"dc"})
    public void deleteComment(
            @ShellOption(defaultValue = "ID Comment") String id
    ) {
        commentService.deleteById(Long.parseLong(id));
        String.format("success delete %s", id);
    }
    @ShellMethod(value = "Get comment", key = {"gc"})
    public void getComment(
            @ShellOption(defaultValue = "ID Comment") String id
    ) {
        System.out.println(commentService.findById(Long.parseLong(id)));
    }
    @ShellMethod(value = "Find comments", key = {"fc"})
    public void findComments(
    ) {
        commentService.findAll().forEach(i -> System.out.println(i.toString()));
    }

}
