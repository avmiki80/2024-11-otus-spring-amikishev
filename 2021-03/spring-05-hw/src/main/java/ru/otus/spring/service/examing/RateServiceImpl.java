package ru.otus.spring.service.examing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.exception.CustomServiceException;
import ru.otus.spring.service.CustomMessageService;

import java.util.Objects;

@Service
public class RateServiceImpl implements RateService{
    private final Long thresholdCorrectAnswer;
    private final CustomMessageService messageService;


    public RateServiceImpl(
            @Value("${threshold_correct_answer}") Long thresholdCorrectAnswer,
            CustomMessageService messageService) {
        this.thresholdCorrectAnswer = thresholdCorrectAnswer;
        this.messageService = messageService;
    }

    @Override
    public String rate(Long correctAnswer) {
        checkThresholdCorrectAnswer(thresholdCorrectAnswer);
        if(correctAnswer >= thresholdCorrectAnswer)
            return messageService.getMessage("result.congratulation");
        return messageService.getMessage("result.try-again");
    }

    private void checkThresholdCorrectAnswer(Long thresholdCorrectAnswer){
        if(Objects.isNull(thresholdCorrectAnswer) || thresholdCorrectAnswer < 0L)
            throw new CustomServiceException(messageService.getMessage("exception.wrong-threshold-correct-answer"));
    }
}
