package ru.otus.spring.hw19.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw19.domain.Water;

@Service
public class CleanWaterServiceImpl implements CleanWaterService{
    private final int CLEAN_FACTOR = 3;
    @Override
    public Water water(Water water) {
        final int  pollutionFactor = water.getPollutionFactor();
        System.out.printf("очистка воды: %s, с коэфициентом загрязнения = %s%n", water.getUuid(), water.getPollutionFactor());
        if ((pollutionFactor - CLEAN_FACTOR) <= 0)
            return new Water(water.getUuid(), 0, water.isToIce());
        return new Water(water.getUuid(), pollutionFactor - CLEAN_FACTOR, water.isToIce());
    }
}
