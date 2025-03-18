package ru.otus.spring.hw19.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class Pack {
    public List<?> objects;
}
