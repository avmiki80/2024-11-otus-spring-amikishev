package ru.otus.spring.hw26.moderator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.h26.model.tomoderate.Comment;
import ru.otus.spring.hw26.moderator.dto.CheckedCommentDto;
import ru.otus.spring.hw26.moderator.dto.ModerateDto;
import ru.otus.spring.hw26.moderator.search.ModerateSearch;
import ru.otus.spring.hw26.moderator.service.CrudService;
import ru.otus.spring.hw26.moderator.service.ModeratorService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/moderator")
@RequiredArgsConstructor
public class ModeratorController {
    private final ModeratorService moderatorService;
    private final CrudService<ModerateDto, ModerateSearch> crudService;
    @PostMapping()
    public ResponseEntity<CheckedCommentDto> moderate(@RequestBody Comment comment){
        return ResponseEntity.ok(moderatorService.moderate(comment));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        crudService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ModerateDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(crudService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<Page<ModerateDto>> find(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "from", required = false) String from,
            @RequestParam(name = "to", required = false) String to,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "dir", required = false) String dir
    ){
        ModerateSearch params = ModerateSearch.builder()
                .text(text)
                .from(from != null ? LocalDateTime.parse(from) : null)
                .to(to != null ? LocalDateTime.parse(to) : null)
                .pageNumber(page)
                .pageSize(size)
                .sortColumn(sort)
                .sortDirection(dir)
                .build();

        return ResponseEntity.ok(crudService.findByParams(params));
    }
}
