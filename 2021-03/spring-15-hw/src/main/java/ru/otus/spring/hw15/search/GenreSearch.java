package ru.otus.spring.hw15.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GenreSearch {
    private String title;
}
