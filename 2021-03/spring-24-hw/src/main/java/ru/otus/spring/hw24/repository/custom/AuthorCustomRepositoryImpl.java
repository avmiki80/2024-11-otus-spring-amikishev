package ru.otus.spring.hw24.repository.custom;

import org.springframework.context.annotation.Lazy;
import ru.otus.spring.hw24.domain.Author;
import ru.otus.spring.hw24.repository.AuthorRepository;
import ru.otus.spring.hw24.search.AuthorSearch;

public class AuthorCustomRepositoryImpl implements AuthorCustomRepository<Author, AuthorSearch> {
    private final AuthorRepository authorRepository;

    public AuthorCustomRepositoryImpl(@Lazy AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author findAndCreateIfAbsent(AuthorSearch params) {
        return authorRepository.findByParams(params.getFirstname(), params.getLastname())
                .stream()
                .findAny()
                .orElseGet(() -> authorRepository.save(new Author(null, params.getFirstname(), params.getLastname())));
    }
}
