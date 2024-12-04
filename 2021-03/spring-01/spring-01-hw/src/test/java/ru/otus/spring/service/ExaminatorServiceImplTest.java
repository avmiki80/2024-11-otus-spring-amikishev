package ru.otus.spring.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import ru.otus.spring.dao.QuestionDao;

import java.util.Collections;

class ExaminatorServiceImplTest {
    private final QuestionService questionService = question -> true;
    private final QuestionDao questionDao = () -> Collections.emptyList();

    private final ExaminatorService examinatorService = new ExaminatorServiceImpl(this.questionService, questionDao);

    @Test
    public void someTest(){
        assertDoesNotThrow(() -> this.examinatorService.testing());
    }
}