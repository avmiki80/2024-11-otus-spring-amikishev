package ru.otus.spring.service;

import ru.otus.spring.dao.Dao;
import ru.otus.spring.domain.Person;

public class PersonServiceImpl implements PersonService {

    private final Dao<Person> dao;

    public PersonServiceImpl(Dao dao) {
        this.dao = dao;
    }

    public Person getByName(String name) {
        return dao.findByParam(name);
    }
}
