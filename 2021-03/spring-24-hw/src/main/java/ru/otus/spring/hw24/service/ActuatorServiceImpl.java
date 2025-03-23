package ru.otus.spring.hw24.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw24.repository.AuthorRepository;
import ru.otus.spring.hw24.repository.BookRepository;
import ru.otus.spring.hw24.repository.CommentRepository;
import ru.otus.spring.hw24.repository.GenreRepository;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ActuatorServiceImpl implements ActuatorService{
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final CommentRepository commentRepository;
    @Override
    public Map<String, Long> getAppInfo() {
        Map<String, Long> details = new HashMap<>();
        details.put("Authors", authorRepository.count());
        details.put("Genres", genreRepository.count());
        details.put("Books", bookRepository.count());
        details.put("Comments", commentRepository.count());
        return details;
    }
}
