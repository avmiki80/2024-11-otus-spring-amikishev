package ru.otus.spring.hw13.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.hw13.domain.Author;
import ru.otus.spring.hw13.domain.Book;
import ru.otus.spring.hw13.domain.Comment;
import ru.otus.spring.hw13.domain.Genre;
import ru.otus.spring.hw13.dto.BookDto;
import ru.otus.spring.hw13.exception.ServiceException;
import ru.otus.spring.hw13.mapper.Mapper;
import ru.otus.spring.hw13.repository.*;
import ru.otus.spring.hw13.repository.custom.CustomRepository;
import ru.otus.spring.hw13.repository.custom.GenreCustomRepository;
import ru.otus.spring.hw13.search.AuthorSearch;
import ru.otus.spring.hw13.search.BookSearch;
import ru.otus.spring.hw13.search.CommentSearch;
import ru.otus.spring.hw13.search.GenreSearch;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService implements CrudService<BookDto, BookSearch> {
    private final BookRepository bookRepository;
    private final CustomRepository<Genre, GenreSearch> genreRepository;
    private final CustomRepository<Author, AuthorSearch> authorRepository;
    private final Mapper<BookDto, Book> bookMapper;

    @Override
    @Transactional
    public BookDto save(BookDto obj) {
        Book book = bookMapper.toEntity(obj);

        Genre persistGenre = genreRepository.findAndCreateIfAbsent(
                GenreSearch.builder().title(obj.getGenre()).build());
        book.setGenre(persistGenre);

        Author persistAuthor = authorRepository.findAndCreateIfAbsent(
                AuthorSearch.builder()
                        .firstname(obj.getFirstname())
                        .lastname(obj.getLastname())
                        .build());
        book.setAuthor(persistAuthor);

        Book persistBook = bookRepository.save(book);

        return bookMapper.toDto(persistBook);
    }

    @Override
    @Transactional
    public BookDto update(long id, BookDto obj) {
        Book persistBook = bookRepository.findById(id).orElseThrow(() -> new ServiceException("exception.object-not-found"));
        persistBook.setTitle(obj.getTitle());

        Genre persistGenre = genreRepository.findAndCreateIfAbsent(
                GenreSearch.builder().title(obj.getGenre()).build());
        persistBook.setGenre(persistGenre);

        Author persistAuthor = authorRepository.findAndCreateIfAbsent(
                AuthorSearch.builder()
                        .firstname(obj.getFirstname())
                        .lastname(obj.getLastname())
                        .build());
        persistBook.setAuthor(persistAuthor);

        return bookMapper.toDto(persistBook);
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
        return bookRepository.findByParams(params.getTitle(), params.getGenre(), params.getFirstname(), params.getLastname()).stream()
                .map(bookMapper::toDto).collect(Collectors.toList());
    }
}
