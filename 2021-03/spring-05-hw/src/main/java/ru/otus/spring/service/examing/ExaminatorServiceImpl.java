package ru.otus.spring.service.examing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.service.CustomMessageService;
import ru.otus.spring.service.ReadWriteService;

@Service
@RequiredArgsConstructor
public class ExaminatorServiceImpl implements ExaminatorService{
    private final TestingService testingService;
    private final RateService rateService;
    private final ReadWriteService readWriteService;
    private final CustomMessageService messageService;

    @Override
    public void examing(String firstname, String lastname) {
        final long result = testingService.testing();
        readWriteService.write(messageService.getMessage("result.result") + " " + firstname + " " + lastname + " = " + result);
        readWriteService.write(rateService.rate(result));
    }
}
