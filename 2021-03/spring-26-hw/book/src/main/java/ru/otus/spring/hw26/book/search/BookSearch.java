package ru.otus.spring.hw26.book.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class BookSearch {
    private String title;
    private String genre;
    private String firstname;
    private String lastname;
}
