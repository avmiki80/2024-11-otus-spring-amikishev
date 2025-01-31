package ru.otus.spring.hw11.repository;

import java.util.List;
import java.util.Optional;

public interface CommonRepository<E, S>{
    E save(E obj);
    void deleteById(long id);
    List<E> findAll();
    Optional<E> findById(long id);
    List<E> findByParams(S params);
}
