package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
    Question get(String question);
    Question save(Question question);
    void remove(Question question);
}
