package ru.otus.spring.dao;

import java.util.List;

public interface Dao<T>{
    Long insert(T obj);
    void update(T obj);
    void deleteById(long id);
    List<T> findAll();
    T findById(long id);
}
