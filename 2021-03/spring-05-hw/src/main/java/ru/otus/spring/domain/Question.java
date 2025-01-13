package ru.otus.spring.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = "answer")
@Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "answer")
public class Question {
    private final String question;
    private final String answer;
}
