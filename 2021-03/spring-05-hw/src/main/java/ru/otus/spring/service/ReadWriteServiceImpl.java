package ru.otus.spring.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Scanner;

@Service
public class ReadWriteServiceImpl implements ReadWriteService {
    private Scanner scanner;
    @PostConstruct
    public void init(){
        scanner = new Scanner(System.in);
    }
    @Override
    public void write(String message) {
        System.out.print(message);
    }

    @Override
    public String read() {
        return scanner.nextLine().trim();
    }
    @PreDestroy
    public void destroy(){
        scanner.close();
    }
}
