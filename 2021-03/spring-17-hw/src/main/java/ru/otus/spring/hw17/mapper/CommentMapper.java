package ru.otus.spring.hw17.mapper;


import org.springframework.stereotype.Component;
import ru.otus.spring.hw17.domain.Book;
import ru.otus.spring.hw17.domain.Comment;
import ru.otus.spring.hw17.dto.CommentDto;

import java.util.Objects;
@Component
public class CommentMapper implements Mapper<CommentDto, Comment>{
    @Override
    public CommentDto toDto(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .text(entity.getText())
                .bookTitle(Objects.isNull(entity.getBook()) || Objects.isNull(entity.getBook().getTitle()) ?
                        "" : entity.getBook().getTitle())
                .build();
    }

    @Override
    public Comment toEntity(CommentDto dto) {
        return new Comment(dto.getId(), dto.getText(), Book.builder().title(dto.getBookTitle()).build());
    }
}
