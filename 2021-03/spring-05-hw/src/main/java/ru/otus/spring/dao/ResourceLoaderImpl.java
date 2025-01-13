package ru.otus.spring.dao;


import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.CustomDaoException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class ResourceLoaderImpl implements ResourceLoader<Question>{
    private final String questionsFilename;

    public ResourceLoaderImpl(String questionsFilename) {
        this.questionsFilename = questionsFilename;
    }

    @Override
    public List<Question> load() {
            InputStream inputStream = getClass().getResourceAsStream(questionsFilename);
            if(Objects.isNull(inputStream))
                throw new CustomDaoException("exception.problem-resource");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            return reader.lines()
                    .map(iter -> iter.split(";"))
                    .filter(iter -> iter.length == 2)
                    .map(iter -> new Question(iter[0], iter[1]))
                    .collect(Collectors.toList());
    }
}
