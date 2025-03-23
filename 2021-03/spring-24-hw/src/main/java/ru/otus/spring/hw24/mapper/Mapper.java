package ru.otus.spring.hw24.mapper;


public interface Mapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
