package ru.otus.spring.hw11.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class AuthorSearch {
    private String firstname;
    private String lastname;
}
