package ru.otus.spring.hw11.repository;

public interface CustomRepository<E, S> {
    E findAndCreateIfAbsent(S params);
}
