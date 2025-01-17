package ru.otus.spring.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.otus.spring.service.examing.ExaminatorService;
@RequiredArgsConstructor
@Component
public class CustomEventListener {
    private final ExaminatorService examinatorService;
    @EventListener
    public void onStartExaming(CustomEvent customEvent){
        examinatorService.examing(customEvent.getMessage().get("firstname"), customEvent.getMessage().get("lastname"));
    }
}
