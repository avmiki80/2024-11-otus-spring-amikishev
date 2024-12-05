package ru.otus.spring.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Collections;

class ExaminatorServiceImplTest {

    private final ExaminatorService examinatorService = new ExaminatorServiceImpl(question -> true, () -> Collections.emptyList());

    @Test
    public void someTest(){
        assertDoesNotThrow(() -> this.examinatorService.testing());
    }
}