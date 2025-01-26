package ru.otus.spring.hw11.mapper;

public interface Mapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
