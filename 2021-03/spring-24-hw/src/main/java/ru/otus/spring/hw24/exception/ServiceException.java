package ru.otus.spring.hw24.exception;

public class ServiceException extends RuntimeException{
    public ServiceException(String message) {
        super(message);
    }
}
