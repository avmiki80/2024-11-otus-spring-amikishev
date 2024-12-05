package ru.otus.spring.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Collections;

class ExaminatorServiceImplTest {

    private final ExaminatorService examinatorService = new ExaminatorServiceImpl(question -> true, () -> Collections.emptyList());

    @Test
    public void someTest(){
        assertAll(
                () -> assertDoesNotThrow(() -> this.examinatorService.testing()),
                () -> assertThat(this.examinatorService.testing()).isEqualTo(0L)
        );
    }
}