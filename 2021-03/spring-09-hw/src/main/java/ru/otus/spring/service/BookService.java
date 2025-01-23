package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.Dao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.search.AuthorSearch;
import ru.otus.spring.search.BookSearch;
import ru.otus.spring.search.GenreSearch;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService implements CrudService<Book, BookSearch>{
    private final Dao<Book, BookSearch> bookDao;
    private final CrudService<Genre, GenreSearch> genreService;
    private final CrudService<Author, AuthorSearch> authorService;
    @Override
    @Transactional
    public Long insert(Book obj) {
        obj.setGenre(genreService.findAndCreateIfAbsent(
                GenreSearch.builder()
                        .title(obj.getGenre().getTitle())
                        .build()
                ));
        obj.setAuthor(authorService.findAndCreateIfAbsent(
                AuthorSearch.builder()
                        .firstname(obj.getAuthor().getFirstname())
                        .lastname(obj.getAuthor().getLastname())
                        .build()
        ));
        return bookDao.insert(obj);
    }
    @Override
    @Transactional
    public void update(Book obj) {
        obj.setGenre(genreService.findAndCreateIfAbsent(
                GenreSearch.builder()
                        .title(obj.getGenre().getTitle())
                        .build()
        ));
        obj.setAuthor(authorService.findAndCreateIfAbsent(
                AuthorSearch.builder()
                        .firstname(obj.getAuthor().getFirstname())
                        .lastname(obj.getAuthor().getLastname())
                        .build()
        ));
        bookDao.update(obj);
    }

    @Override
    public void deleteById(long id) {
        try {
            bookDao.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Book findById(long id) {
        try {
            return bookDao.findById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Book> findByParams(BookSearch params) {
        return bookDao.findByParams(params);
    }

    @Override
    public Book findAndCreateIfAbsent(BookSearch params) {
        throw new UnsupportedOperationException("The method is not needed, but may be needed in the future");
    }

}
