package ru.otus.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.ExaminatorService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("/spring-context.xml");
        ExaminatorService service = context.getBean(ExaminatorService.class);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Lastname: ");
        String lastname = scanner.nextLine().trim();
        System.out.println("Result " + name + " " + lastname + " = " + service.testing());
        scanner.close();
    }
}