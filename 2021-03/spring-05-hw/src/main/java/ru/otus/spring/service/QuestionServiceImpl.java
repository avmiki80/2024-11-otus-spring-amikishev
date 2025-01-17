package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CustomDaoException;
import ru.otus.spring.exception.CustomServiceException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{
    private final QuestionDao questionDao;
    private final CustomMessageService customMessageService;

    @Override
    public List<Question> findAll() {
        return questionDao.findAll();
    }

    @Override
    public Question get(String question) {
        try {
            return questionDao.get(question);
        } catch (CustomDaoException e){
            throw new CustomServiceException(customMessageService.getMessage(e.getMessage()));
        }
    }

    @Override
    public Question save(Question question) {
        return questionDao.save(question);
    }

    @Override
    public void remove(Question question) {
        try {
            questionDao.remove(question);
        } catch (CustomDaoException e){
            throw new CustomServiceException(customMessageService.getMessage(e.getMessage()));
        }
    }
}
