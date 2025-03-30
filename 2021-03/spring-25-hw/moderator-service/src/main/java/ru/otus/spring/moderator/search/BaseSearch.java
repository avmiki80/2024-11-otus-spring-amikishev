package ru.otus.spring.moderator.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
@Getter @Setter
@SuperBuilder
@AllArgsConstructor
public class BaseSearch implements Serializable {

    private Integer pageNumber;
    private Integer pageSize;
    private String sortColumn;
    private String sortDirection;
}
