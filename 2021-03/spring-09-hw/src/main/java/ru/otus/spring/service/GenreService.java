package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.dao.Dao;
import ru.otus.spring.dao.GenreDao;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.search.GenreSearch;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService implements CrudService<Genre, GenreSearch>{
    private final Dao<Genre, GenreSearch> genreDao;

    @Override
    public Long insert(Genre obj) {
        return genreDao.insert(obj);
    }

    @Override
    public void update(Genre obj) {
        genreDao.update(obj);
    }

    @Override
    public void deleteById(long id) {
        try {
            genreDao.deleteById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    @Override
    public Genre findById(long id) {
        try {
            return genreDao.findById(id);
        } catch (Exception e){
            throw new ServiceException(e.getMessage());
        }
    }
    public List<Genre> findByParams(GenreSearch params) {
        return genreDao.findByParams(params);
    }
    @Transactional
    public Genre findAndCreateIfAbsent(GenreSearch params) {
        Optional<Genre> genreOptional = findByParams(params).stream().findAny();
        if(genreOptional.isPresent())
            return genreOptional.get();
        else {
            Genre genre = new Genre(null, params.getTitle());
            genre.setId(insert(genre));
            return genre;
        }
    }
}
