package ru.otus.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.CustomMessageService;
import ru.otus.spring.service.CustomMessageServiceImpl;

@TestConfiguration
@PropertySource("classpath:application.properties")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
public class BaseServiceConfigTest {
    @Autowired
    private MessageSource messageSource;
    @Bean
    public CustomMessageService customMessageService(){
        return new CustomMessageServiceImpl(messageSource);
    }
}
