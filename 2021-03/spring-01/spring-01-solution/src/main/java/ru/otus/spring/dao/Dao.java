package ru.otus.spring.dao;

public interface Dao<T>{

    T findByParam(String param);
}
