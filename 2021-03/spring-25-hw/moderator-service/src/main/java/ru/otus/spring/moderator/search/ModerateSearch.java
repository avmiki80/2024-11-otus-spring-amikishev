package ru.otus.spring.moderator.search;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ModerateSearch extends BaseSearch{
    private String text;
    private LocalDateTime from;
    private LocalDateTime to;

}
