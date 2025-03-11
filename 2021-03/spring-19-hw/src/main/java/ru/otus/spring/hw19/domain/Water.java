package ru.otus.spring.hw19.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class Water {
    private UUID uuid;
    private int pollutionFactor;
    private boolean toIce;
}
