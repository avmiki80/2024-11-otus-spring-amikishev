package ru.otus.spring.domain;

public class Question {
    private String question;
    private String answer;

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
}
