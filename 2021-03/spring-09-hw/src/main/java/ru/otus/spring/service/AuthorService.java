package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.dao.Dao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.search.AuthorSearch;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<Author, AuthorSearch>{
    private final Dao<Author, AuthorSearch> authorDao;
    @Override
    public Long insert(Author obj) {
        return authorDao.insert(obj);
    }

    @Override
    public void update(Author obj) {
        authorDao.update(obj);
    }

    @Override
    public void deleteById(long id) {
        try {
            authorDao.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Author> findAll() {
        return authorDao.findAll();
    }

    @Override
    public Author findById(long id) {
        try{
            return authorDao.findById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    @Override
    public List<Author> findByParams(AuthorSearch params) {
        return authorDao.findByParams(params);
    }

    @Override
    @Transactional
    public Author findAndCreateIfAbsent(AuthorSearch params) {
        Optional<Author> authorOptional = findByParams(
                params
        ).stream().findAny();
        if(authorOptional.isPresent()){
            return authorOptional.get();
        } else {
            Author author = new Author(null, params.getFirstname(), params.getLastname());
            author.setId(insert(author));
            return author;
        }
    }
}
