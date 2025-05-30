package ru.otus.spring.book.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.book.domain.Genre;
import ru.otus.spring.book.dto.GenreDto;

@Component
@RequiredArgsConstructor
public class GenreMapper implements Mapper<GenreDto, Genre>{
    private final ObjectMapper objectMapper;
    @Override
    public GenreDto toDto(Genre entity) {
        return objectMapper.convertValue(entity, GenreDto.class);
    }

    @Override
    public Genre toEntity(GenreDto dto) {
        return objectMapper.convertValue(dto, Genre.class);
    }
}
