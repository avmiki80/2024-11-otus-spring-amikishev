package ru.otus.spring.book.service;

import ru.otus.spring.book.dto.CommentDto;
import ru.otus.spring.book.dto.ModerateDto;

public interface ModeratorService {
    ModerateDto toModerate(CommentDto commentDto);
}
