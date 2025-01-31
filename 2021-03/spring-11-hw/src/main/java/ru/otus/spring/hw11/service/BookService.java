package ru.otus.spring.hw11.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw11.domain.Author;
import ru.otus.spring.hw11.domain.Book;
import ru.otus.spring.hw11.domain.Comment;
import ru.otus.spring.hw11.domain.Genre;
import ru.otus.spring.hw11.dto.BookDto;
import ru.otus.spring.hw11.exception.ServiceException;
import ru.otus.spring.hw11.mapper.Mapper;
import ru.otus.spring.hw11.repository.*;
import ru.otus.spring.hw11.search.AuthorSearch;
import ru.otus.spring.hw11.search.BookSearch;
import ru.otus.spring.hw11.search.CommentSearch;
import ru.otus.spring.hw11.search.GenreSearch;

import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService implements CrudService<BookDto, BookSearch> {
    private final CommonRepository<Book, BookSearch> bookRepository;
    private final CustomRepository<Genre, GenreSearch> genreRepository;
    private final CustomRepository<Author, AuthorSearch> authorRepository;
    private final CommonRepository<Comment, CommentSearch> commentRepository;
    private final Mapper<BookDto, Book> bookMapper;

    @Override
    @Transactional
    public BookDto save(BookDto obj) {
        Book book = bookMapper.toEntity(obj);

        Genre persistGenre = genreRepository.findAndCreateIfAbsent(
                GenreSearch.builder().title(obj.getGenre()).build());

        Author persistAuthor = authorRepository.findAndCreateIfAbsent(
                AuthorSearch.builder()
                        .firstname(obj.getFirstname())
                        .lastname(obj.getLastname())
                        .build());

        if (Objects.nonNull(obj.getId()) && !obj.getId().equals(0L)) {
            book.setComments(commentRepository.findByParams(
                    CommentSearch.builder()
                            .bookIds(Collections.singleton(obj.getId()))
                            .build()));
        }

        book.setGenre(persistGenre);
        book.setAuthor(persistAuthor);

        Book persistBook = bookRepository.save(book);

        return bookMapper.toDto(persistBook);
    }

    @Override
    public BookDto update(long id, BookDto obj) {
        throw new UnsupportedOperationException("The method is not needed now, but must have for REST API in future");
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        try {
            bookRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> findByParams(BookSearch params) {
        return bookRepository.findByParams(params).stream().map(bookMapper::toDto).collect(Collectors.toList());
    }
}
