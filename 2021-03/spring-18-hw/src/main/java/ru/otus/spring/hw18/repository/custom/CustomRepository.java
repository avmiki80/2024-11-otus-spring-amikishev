package ru.otus.spring.hw18.repository.custom;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
