package ru.otus.spring.hw19.service;

import ru.otus.spring.hw19.domain.Pack;

import java.util.List;

public interface PackageService<T> {
    Pack packaging(List<T> objects);
}
