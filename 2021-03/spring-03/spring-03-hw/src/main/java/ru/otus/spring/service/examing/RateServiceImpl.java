package ru.otus.spring.service.examing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.exception.ServiceException;

import java.util.Objects;

import static ru.otus.spring.message.ServiceMessage.*;

@Service
public class RateServiceImpl implements RateService{
    private final Long thresholdCorrectAnswer;

    public RateServiceImpl(@Value("${threshold_correct_answer}") Long thresholdCorrectAnswer) {
        this.thresholdCorrectAnswer = thresholdCorrectAnswer;
    }

    @Override
    public String rate(Long correctAnswer) {
        checkThresholdCorrectAnswer(thresholdCorrectAnswer);
        if(correctAnswer >= thresholdCorrectAnswer)
            return CONGRATULATION;
        return TRY_IT_AGAIN;
    }

    private void checkThresholdCorrectAnswer(Long thresholdCorrectAnswer){
        if(Objects.isNull(thresholdCorrectAnswer) || thresholdCorrectAnswer < 0L)
            throw new ServiceException(WRONG_THRESHOLD_CORRECT_ANSWER);
    }
}
