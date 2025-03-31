package ru.otus.spring.book.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class GenreSearch {
    private String title;
}
