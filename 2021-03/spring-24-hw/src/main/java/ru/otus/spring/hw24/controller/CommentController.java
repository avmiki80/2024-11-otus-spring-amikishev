package ru.otus.spring.hw24.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw24.dto.CommentDto;
import ru.otus.spring.hw24.search.CommentSearch;
import ru.otus.spring.hw24.service.CrudService;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CrudService<CommentDto, CommentSearch> commentService;

    @PostMapping()
    public ResponseEntity<CommentDto> save(@RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.save(commentDto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(@PathVariable(name = "id") Long id, @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentService.update(id, commentDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<List<CommentDto>> find(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "bookTitle", required = false) String bookTitle
    ){
        return ResponseEntity.ok(commentService.findByParams(CommentSearch.builder()
                .text(text)
                .bookTitle(bookTitle)
                .build()));
    }
}
