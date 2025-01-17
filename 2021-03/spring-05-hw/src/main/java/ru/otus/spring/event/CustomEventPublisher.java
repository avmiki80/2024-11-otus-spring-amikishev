package ru.otus.spring.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class CustomEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void startExaming(final Map<String, String> message){
        CustomEvent customEvent = new CustomEvent(this, message);
        publisher.publishEvent(customEvent);
    }
}
