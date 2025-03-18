package ru.otus.spring.hw19.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw19.domain.Pack;
import ru.otus.spring.hw19.domain.Steam;

import java.util.List;

@Service
public class SteamPackageService implements PackageService<Steam>{
    @Override
    public Pack packaging(List<Steam> steamList) {
        System.out.println("Упаковывается: " + steamList.toString());
        return new Pack(steamList);
    }
}
