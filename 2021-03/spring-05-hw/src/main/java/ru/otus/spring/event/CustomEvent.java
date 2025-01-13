package ru.otus.spring.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Map;
@Getter
public class CustomEvent extends ApplicationEvent {
    private final Map<String, String> message;
    public CustomEvent(Object source, Map<String, String> message) {
        super(source);
        this.message = message;
    }
}
