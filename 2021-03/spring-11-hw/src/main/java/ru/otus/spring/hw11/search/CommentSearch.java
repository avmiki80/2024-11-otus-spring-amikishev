package ru.otus.spring.hw11.search;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;

@Builder
@Getter
@EqualsAndHashCode
public class CommentSearch {
    private String text;
    private String bookTitle;
    private Collection<Long> bookIds;
}
