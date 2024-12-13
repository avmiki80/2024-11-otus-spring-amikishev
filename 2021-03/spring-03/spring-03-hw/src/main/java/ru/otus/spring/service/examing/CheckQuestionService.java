package ru.otus.spring.service.examing;

import ru.otus.spring.domain.Question;

public interface CheckQuestionService {
    Boolean checkQuestion(Question question);
}
