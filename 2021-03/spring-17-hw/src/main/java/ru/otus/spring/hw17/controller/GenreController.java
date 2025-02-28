package ru.otus.spring.hw17.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.hw17.dto.GenreDto;
import ru.otus.spring.hw17.search.GenreSearch;
import ru.otus.spring.hw17.service.CrudService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {
    private final CrudService<GenreDto, GenreSearch> genreService;

    @PostMapping("/genre")
    public ResponseEntity<GenreDto> save(@RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.save(genreDto));
    }
    @PutMapping("/genre/{id}")
    public ResponseEntity<GenreDto> update(@PathVariable(name = "id") Long id, @RequestBody GenreDto genreDto){
        return ResponseEntity.ok(genreService.update(id, genreDto));
    }
    @DeleteMapping("/genre/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id){
        genreService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/genre/{id}")
    public ResponseEntity<GenreDto> findById(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(genreService.findById(id));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<GenreDto>> find(
            @RequestParam(name = "title", required = false) String title
    ){
        return ResponseEntity.ok(genreService.findByParams(GenreSearch.builder()
                .title(title)
                .build()));
    }
}
