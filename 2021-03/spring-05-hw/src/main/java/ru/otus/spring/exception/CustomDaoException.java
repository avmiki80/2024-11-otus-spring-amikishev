package ru.otus.spring.exception;

public class CustomDaoException extends RuntimeException{
    public CustomDaoException(String message) {
        super(message);
    }
}
