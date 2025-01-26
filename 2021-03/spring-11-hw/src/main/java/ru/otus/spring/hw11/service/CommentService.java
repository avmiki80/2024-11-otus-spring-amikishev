package ru.otus.spring.hw11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw11.domain.Book;
import ru.otus.spring.hw11.domain.Comment;
import ru.otus.spring.hw11.dto.CommentDto;
import ru.otus.spring.hw11.exception.ServiceException;
import ru.otus.spring.hw11.mapper.Mapper;
import ru.otus.spring.hw11.repository.CommonRepository;
import ru.otus.spring.hw11.search.BookSearch;
import ru.otus.spring.hw11.search.CommentSearch;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService implements CrudService<CommentDto, CommentSearch>{
    private final CommonRepository<Comment, CommentSearch> commentRepository;
    private final CommonRepository<Book, BookSearch> bookRepository;
    private final Mapper<CommentDto, Comment> commentMapper;
    @Override
    @Transactional
    public CommentDto save(CommentDto obj) {

        Book persistBook = bookRepository.findByParams(
                BookSearch.builder()
                        .title(obj.getBookTitle())
                        .build())
                .stream()
                .findAny().orElseThrow(() -> new ServiceException("exception.object-not-found"));

        Comment comment = commentMapper.toEntity(obj);
        comment.setBook(persistBook);

        Comment persistComment = commentRepository.save(comment);
        persistBook.getComments().add(persistComment);

        return commentMapper.toDto(persistComment);
    }

    @Override
    public CommentDto update(long id, CommentDto obj) {
        throw new UnsupportedOperationException("The method is not needed now, but must have for REST API in future");
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            commentRepository.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAll() {
        return commentRepository.findAll().stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(long id) {
        return commentMapper.toDto(commentRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findByParams(CommentSearch params) {
        return commentRepository.findByParams(params).stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }
}
