package ru.otus.spring.moderator.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.*;
import ru.otus.spring.moderator.domain.Moderate;
import ru.otus.spring.moderator.dto.ModerateDto;
import ru.otus.spring.moderator.mapper.Mapper;
import ru.otus.spring.moderator.mapper.ModerateMapper;
import ru.otus.spring.moderator.repository.ModerateRepository;
import ru.otus.spring.moderator.search.ModerateSearch;
import ru.otus.spring.moderator.service.CrudService;
import ru.otus.spring.moderator.service.CrudServiceImpl;



@TestConfiguration
@ComponentScan(
        basePackages = "ru.otus.spring.moderator.config"
)
public class ConfigTest {

    @Autowired
    private ModerateRepository moderateRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Mapper<ModerateDto, Moderate> moderateMapper(){
        return new ModerateMapper(objectMapper);
    }
    @Bean
    public CrudService<ModerateDto, ModerateSearch> moderateService(){
        return new CrudServiceImpl(moderateRepository, moderateMapper());
    }

}
