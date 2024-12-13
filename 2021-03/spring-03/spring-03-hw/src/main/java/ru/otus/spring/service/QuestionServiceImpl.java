package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.List;
import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final QuestionDao questionDao;
    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> findAll() {
        return questionDao.findAll();
    }

    @Override
    public Question get(String question) {
        return questionDao.get(question);
    }

    @Override
    public Question save(Question question) {
        return questionDao.save(question);
    }

    @Override
    public void remove(Question question) {
        questionDao.remove(question);
    }
}
