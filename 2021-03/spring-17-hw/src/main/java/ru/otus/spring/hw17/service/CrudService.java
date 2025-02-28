package ru.otus.spring.hw17.service;

import java.util.List;

public interface CrudService<D, S> {
    D save(D obj);
    D update(long id, D obj);
    void deleteById(long id);
    List<D> findAll();
    D findById(long id);
    List<D> findByParams(S params);
}
