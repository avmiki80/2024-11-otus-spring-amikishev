package ru.otus.spring.cache;

public interface CacheDao<T> {
    boolean isCacheable(String param);
    T get(String param);
    void put(String param, T obj);
}
