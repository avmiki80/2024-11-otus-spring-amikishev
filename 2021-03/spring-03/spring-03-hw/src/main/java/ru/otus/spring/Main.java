package ru.otus.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.spring.service.examing.ExaminatorService;

@ComponentScan(basePackages = "ru.otus.spring")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Main.class);
        ExaminatorService examinatorService = context.getBean(ExaminatorService.class);
        examinatorService.examing();
    }
}