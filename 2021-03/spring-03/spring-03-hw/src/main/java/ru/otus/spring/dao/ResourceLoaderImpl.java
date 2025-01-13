package ru.otus.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.ServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.message.ServiceMessage.PROBLEM_RESOURCE;

@Component
public class ResourceLoaderImpl implements ResourceLoader<Question>{
    private final Resource resource;
    @Autowired
    public ResourceLoaderImpl(Resource resource) {
        this.resource = resource;
    }

    @Override
    public List<Question> load() {
        try {
            return Files.readAllLines(resource.getFile().toPath()).stream()
                    .map(iter -> iter.split(";"))
                    .filter(iter -> iter.length == 2)
                    .map(iter -> new Question(iter[0], iter[1]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ServiceException(PROBLEM_RESOURCE);
        }
    }
}
