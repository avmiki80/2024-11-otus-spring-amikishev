package ru.otus.spring.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CustomDaoException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class QuestionDaoImpl implements QuestionDao{
    private final ResourceLoader<Question> resourceLoader;
    private List<Question> questions;

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
        return getQuestion(question).orElseThrow(() -> new CustomDaoException("exception.question-not-found"));
    }

    private Optional<Question> getQuestion(String question){
        return questions.stream()
                .filter(q -> q.getQuestion().equalsIgnoreCase(question))
                .findFirst();
    }

    @Override
    public Question save(Question question) {
        getQuestion(question.getQuestion()).ifPresent(q -> questions.remove(q));
        questions.add(question);
        return question;
    }

    @Override
    public void remove(Question question) {
        questions.remove(get(question.getQuestion()));
    }

}
