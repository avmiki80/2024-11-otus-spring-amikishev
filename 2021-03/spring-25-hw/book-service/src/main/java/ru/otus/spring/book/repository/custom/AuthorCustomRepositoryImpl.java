package ru.otus.spring.book.repository.custom;

import org.springframework.context.annotation.Lazy;
import ru.otus.spring.book.domain.Author;
import ru.otus.spring.book.repository.AuthorRepository;
import ru.otus.spring.book.search.AuthorSearch;

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
