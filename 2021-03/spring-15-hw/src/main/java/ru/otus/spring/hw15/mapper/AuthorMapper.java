package ru.otus.spring.hw15.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.hw15.domain.Author;
import ru.otus.spring.hw15.dto.AuthorDto;

@Component
public class AuthorMapper implements Mapper<AuthorDto, Author>{
    @Override
    public AuthorDto toDto(Author entity) {
        final AuthorDto dto = new AuthorDto();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        return dto;
    }

    @Override
    public Author toEntity(AuthorDto dto) {
        final Author author = new Author();
        author.setId(dto.getId());
        author.setFirstname(dto.getFirstname());
        author.setLastname(dto.getLastname());
        return author;
    }
}
