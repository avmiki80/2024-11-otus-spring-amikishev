package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.Dao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.ServiceException;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService implements CrudService<Book>{
    private final Dao<Book> bookDao;
    private final GenreService genreService;
    private final AuthorService authorService;
    @Override
    @Transactional
    public Long insert(Book obj) {
        obj.setGenre(genreService.findOrCreateGenre(obj.getGenre().getTitle()));
        obj.setAuthor(authorService.findOrCreateAuthor(obj.getAuthor().getFirstname(), obj.getAuthor().getLastname()));
        return bookDao.insert(obj);
    }
    @Override
    @Transactional
    public void update(Book obj) {
        obj.setGenre(genreService.findOrCreateGenre(obj.getGenre().getTitle()));
        obj.setAuthor(authorService.findOrCreateAuthor(obj.getAuthor().getFirstname(), obj.getAuthor().getLastname()));
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

}
