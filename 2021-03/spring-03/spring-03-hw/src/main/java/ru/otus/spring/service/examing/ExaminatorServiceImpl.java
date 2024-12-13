package ru.otus.spring.service.examing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.service.ReadWriteService;

@Service
public class ExaminatorServiceImpl implements ExaminatorService{
    private final TestingService testingService;
    private final RateService rateService;
    private final ReadWriteService readWriteService;

    @Autowired
    public ExaminatorServiceImpl(TestingService testingService, RateService rateService, ReadWriteService readWriteService) {
        this.testingService = testingService;
        this.rateService = rateService;
        this.readWriteService = readWriteService;
    }

    @Override
    public void examing() {
        readWriteService.write("Name: ");
        String name = readWriteService.read();
        readWriteService.write("Lastname: ");
        String lastname = readWriteService.read();
        final long result = testingService.testing();
        readWriteService.write("Result " + name + " " + lastname + " = " + result);
        readWriteService.write(rateService.rate(result));
    }
}
