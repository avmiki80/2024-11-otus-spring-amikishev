package ru.otus.spring.domain;

import java.util.Objects;

public class Question {
    private final String question;
    private final String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question1 = (Question) o;
        return Objects.equals(getQuestion(), question1.getQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestion());
    }
}
