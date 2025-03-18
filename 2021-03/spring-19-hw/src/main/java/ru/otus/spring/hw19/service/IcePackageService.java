package ru.otus.spring.hw19.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.hw19.domain.Ice;
import ru.otus.spring.hw19.domain.Pack;

import java.util.List;

@Service
public class IcePackageService implements PackageService<Ice>{
    @Override
    public Pack packaging(List<Ice> iceList) {
        System.out.println("Упаковывается: " + iceList.toString());
        return new Pack(iceList);
    }
}
