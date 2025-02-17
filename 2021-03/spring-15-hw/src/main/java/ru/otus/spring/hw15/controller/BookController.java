package ru.otus.spring.hw15.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw15.dto.BookDto;
import ru.otus.spring.hw15.search.BookSearch;
import ru.otus.spring.hw15.service.CrudService;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class BookController {
    private final CrudService<BookDto, BookSearch> bookService;

    @PostMapping("/book")
    public ResponseEntity<BookDto> save(@RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.save(bookDto));
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> update(@PathVariable(name = "id") Long id, @RequestBody BookDto bookDto){
        return ResponseEntity.ok(bookService.update(id, bookDto));
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        bookService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookDto>> find(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "genre", required = false) String genre,
            @RequestParam(name = "firstname", required = false) String firstname,
            @RequestParam(name = "lastname", required = false) String lastname
    ){
        return ResponseEntity.ok(bookService.findByParams(BookSearch.builder()
                        .title(title).genre(genre)
                        .firstname(firstname)
                        .lastname(lastname)
                        .build()));
    }
}
