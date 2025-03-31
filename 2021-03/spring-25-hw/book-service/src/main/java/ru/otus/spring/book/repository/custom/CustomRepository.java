package ru.otus.spring.book.repository.custom;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
