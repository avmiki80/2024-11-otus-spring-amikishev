package ru.otus.spring.hw19;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.spring.hw19.service.BusinessService;

@SpringBootApplication
public class Spring19HwApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Spring19HwApplication.class, args);
        BusinessService service = context.getBean("customBusinessService", BusinessService.class);
        service.customLogic();
        context.close();
    }

}
