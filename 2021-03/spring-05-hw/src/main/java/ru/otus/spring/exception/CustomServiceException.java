package ru.otus.spring.exception;

public class CustomServiceException extends RuntimeException{
    public CustomServiceException(String message) {
        super(message);
    }
}
