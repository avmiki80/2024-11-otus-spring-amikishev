package ru.otus.spring.moderator.service;

import ru.otus.spring.moderator.dto.CommentDto;
import ru.otus.spring.moderator.dto.CheckedCommentDto;

public interface ModeratorService {
    CheckedCommentDto moderate(CommentDto commentDto);
}
