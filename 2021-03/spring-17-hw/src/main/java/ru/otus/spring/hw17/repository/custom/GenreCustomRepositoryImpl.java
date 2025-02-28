package ru.otus.spring.hw17.repository.custom;

import org.springframework.context.annotation.Lazy;
import ru.otus.spring.hw17.domain.Genre;
import ru.otus.spring.hw17.repository.GenreRepository;
import ru.otus.spring.hw17.search.GenreSearch;

public class GenreCustomRepositoryImpl implements GenreCustomRepository<Genre, GenreSearch> {
    private final GenreRepository genreRepository;

    public GenreCustomRepositoryImpl(@Lazy GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre findAndCreateIfAbsent(GenreSearch params) {
        return genreRepository.findByParams(params.getTitle())
                .stream()
                .findAny().orElseGet(() ->
                        genreRepository.save(new Genre(null, params.getTitle())));
    }
}
