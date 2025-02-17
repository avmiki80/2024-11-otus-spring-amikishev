package ru.otus.spring.hw15.repository.custom;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
