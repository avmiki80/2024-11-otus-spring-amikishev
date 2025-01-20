package ru.otus.spring.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private Long id;
    private String title;

    private Genre genre;

    private Author author;
}
