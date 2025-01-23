package ru.otus.spring.service;

import java.util.List;

public interface CrudService<T, S> {
    Long insert(T obj);
    void update(T obj);
    void deleteById(long id);
    List<T> findAll();
    T findById(long id);
    List<T> findByParams(S params);
    T findAndCreateIfAbsent(S params);
}
