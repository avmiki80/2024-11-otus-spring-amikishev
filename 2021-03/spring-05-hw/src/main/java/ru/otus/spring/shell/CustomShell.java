package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.event.CustomEventPublisher;

import java.util.HashMap;
import java.util.Map;

@ShellComponent
@RequiredArgsConstructor
public class CustomShell {
    private String firstname;
    private String lastname;

//    private final ExaminatorService examinatorService;
    private final CustomEventPublisher publisher;

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(
            @ShellOption(defaultValue = "AnyFirstname") String firstname,
            @ShellOption(defaultValue = "AnyLastname") String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
        return String.format("Добро пожаловать: %s %s", firstname, lastname);
    }
    @ShellMethod(value = "Start Examing event command", key = {"s", "start"})
    @ShellMethodAvailability(value = "checkLogin")
    public void publishEvent() {
        final Map<String, String> message = new HashMap<>();
        message.put("firstname", firstname);
        message.put("lastname", lastname);
        publisher.startExaming(message);
//        examinatorService.examing(firstname, lastname);

    }

    private Availability checkLogin() {
        return firstname == null || lastname == null ? Availability.unavailable("Сначала залогиньтесь"): Availability.available();
    }
}
