package ru.otus.spring.hw26.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@Configuration
@IntegrationComponentScan({
        "ru.otus.spring.hw26.book.event.moderate.publish"
})
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }

}
