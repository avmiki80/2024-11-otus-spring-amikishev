package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.AuthorDao;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.exception.ServiceException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService implements CrudService<Author>{
    private final AuthorDao authorDao;
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

    public List<Author> getByFirstnameAndLastname(String firstname, String lastname) {
        return authorDao.getByFirstnameAndLastname(firstname, lastname);
    }
    public Author findOrCreateAuthor(String firstname, String lastname) {
        Optional<Author> authorOptional = getByFirstnameAndLastname(firstname, lastname).stream().findAny();
        if(authorOptional.isPresent()){
            return authorOptional.get();
        } else {
            Author author = new Author(null, firstname, lastname);
            author.setId(insert(author));
            return author;
        }
    }
}
