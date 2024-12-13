package ru.otus.spring.service;

public interface ReadWriteService {
    void write(String message);
    String read();
}
