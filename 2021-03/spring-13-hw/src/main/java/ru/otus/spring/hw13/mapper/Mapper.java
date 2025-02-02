package ru.otus.spring.hw13.mapper;

public interface Mapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
