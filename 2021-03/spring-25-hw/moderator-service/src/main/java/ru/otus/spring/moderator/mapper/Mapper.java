package ru.otus.spring.moderator.mapper;


public interface Mapper<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
