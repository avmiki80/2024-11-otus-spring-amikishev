package ru.otus.spring.hw19.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.integration.annotation.IntegrationComponentScan;
import ru.otus.spring.hw19.domain.Ice;
import ru.otus.spring.hw19.domain.Steam;
import ru.otus.spring.hw19.service.*;

@TestConfiguration
@ComponentScan(
        basePackages = {"ru.otus.spring.hw19"}
)
public class ConfigTest {
    @Bean
    public CleanWaterService cleanWaterServiceImpl(){
        return new CleanWaterServiceImpl();
    }
    @Bean
    public PackageService<Ice> icePackageService(){
        return new IcePackageService();
    }
    @Bean
    public PackageService<Steam> steamPackageService(){
        return new SteamPackageService();
    }
}
