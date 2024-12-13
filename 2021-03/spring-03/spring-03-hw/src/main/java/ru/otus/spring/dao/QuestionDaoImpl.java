package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.otus.spring.message.ServiceMessage.PROBLEM_RESOURCE;
import static ru.otus.spring.message.ServiceMessage.QUESTION_NOT_FOUND;

@Component
public class QuestionDaoImpl implements QuestionDao{
    private final ResourceLoader<Question> resourceLoader;
    private List<Question> questions;
    @Autowired
    public QuestionDaoImpl(ResourceLoader<Question> resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @PostConstruct
    public void init() {
        questions = resourceLoader.load();
    }
    @Override
    public List<Question> findAll() {
        return questions;
    }

    @Override
    public Question get(String question) {
        return getOptional(question).orElseThrow(() -> new ServiceException(QUESTION_NOT_FOUND));
    }

    private Optional<Question> getOptional(String question){
        return questions.stream()
                .filter(q -> q.getQuestion().equalsIgnoreCase(question))
                .findFirst();
    }

    @Override
    public Question save(Question question) {
        getOptional(question.getQuestion()).ifPresent(q -> questions.remove(q));
        questions.add(question);
        return question;
    }

    @Override
    public void remove(Question question) {
        questions.remove(get(question.getQuestion()));
    }

}
