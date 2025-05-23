package ru.otus.spring.moderator.service;

import org.springframework.data.domain.Page;
import ru.otus.spring.moderator.search.BaseSearch;

import java.util.List;

public interface CrudService<D, S extends BaseSearch> {
    D save(D obj);
    D update(long id, D obj);
    void deleteById(long id);
    List<D> findAll();
    D findById(long id);
    Page<D> findByParams(S params);
}
