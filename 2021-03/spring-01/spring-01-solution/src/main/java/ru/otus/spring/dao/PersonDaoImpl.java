package ru.otus.spring.dao;

import ru.otus.spring.cache.CacheDao;
import ru.otus.spring.domain.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonDaoImpl implements Dao<Person> {

    private final CacheDao<Person> cacheDao;
    private List<Person> persons = new ArrayList<>(Arrays.asList(new Person("Ivan", 18), new Person("Vasya", 20)));

    public PersonDaoImpl(CacheDao<Person> cacheDao) {
        this.cacheDao = cacheDao;
    }

    public Person findByParam(String name) {
        Person result;
        if (cacheDao.isCacheable(name))
            return cacheDao.get(name);
        result = persons.stream().filter(person -> person.getName().trim().equalsIgnoreCase(name)).findAny().orElseThrow(() -> new RuntimeException("Person not found"));
        cacheDao.put(name, result);
        return result;
    }
}
