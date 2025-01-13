package ru.otus.spring.service.examing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.ReadWriteService;

@Service
public class CheckQuestionServiceImpl implements CheckQuestionService {
    private final ReadWriteService readWriteService;
    @Autowired
    public CheckQuestionServiceImpl(ReadWriteService readWriteService) {
        this.readWriteService = readWriteService;
    }

    @Override
    public Boolean checkQuestion(Question question) {
        readWriteService.write(question.getQuestion() + "? ");
        return readWriteService.read().equals(question.getAnswer());
    }
}
