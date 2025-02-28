package ru.otus.spring.hw17.repository.custom;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
