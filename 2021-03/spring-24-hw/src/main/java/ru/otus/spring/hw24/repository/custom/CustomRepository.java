package ru.otus.spring.hw24.repository.custom;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
