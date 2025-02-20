package ru.otus.spring.hw15.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw15.domain.Genre;
import ru.otus.spring.hw15.dto.GenreDto;
@Component
public class GenreMapper implements Mapper<GenreDto, Genre>{
    @Override
    public GenreDto toDto(Genre entity) {
        final GenreDto dto = new GenreDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    @Override
    public Genre toEntity(GenreDto dto) {
        final Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setTitle(dto.getTitle());
        return genre;
    }
}
