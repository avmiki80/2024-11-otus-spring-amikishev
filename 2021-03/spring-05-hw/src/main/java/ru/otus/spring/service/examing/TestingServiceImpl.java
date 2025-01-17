package ru.otus.spring.service.examing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;

@Service
public class TestingServiceImpl implements TestingService{
    private final CheckQuestionService checkQuestionService;
    private final QuestionDao questionDao;
    @Autowired
    public TestingServiceImpl(CheckQuestionService checkQuestionService, QuestionDao questionDao) {
        this.checkQuestionService = checkQuestionService;
        this.questionDao = questionDao;
    }

    @Override
    public Long testing() {
        return questionDao.findAll().stream()
                .map(checkQuestionService::checkQuestion)
                .filter(iter -> iter.equals(true))
                .count();
    }
}
