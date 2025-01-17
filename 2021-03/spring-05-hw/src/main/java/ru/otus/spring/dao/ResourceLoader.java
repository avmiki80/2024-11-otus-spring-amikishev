package ru.otus.spring.dao;

import java.util.List;

public interface ResourceLoader<T> {
    List<T> load();
}
