package ru.otus.spring.dao;

import ru.otus.spring.cache.CacheDao;
import ru.otus.spring.domain.Email;
import ru.otus.spring.domain.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailDaoImpl implements Dao<Email>{
    private final CacheDao<Email> cacheDao;
    private List<Email> emails = new ArrayList<>(Arrays.asList(new Email("1@mail.ru"), new Email("2@mail.ru")));

    public EmailDaoImpl(CacheDao<Email> cacheDao) {
        this.cacheDao = cacheDao;
    }

    @Override
    public Email findByParam(String email) {
        Email result;
        if (cacheDao.isCacheable(email))
            return cacheDao.get(email);
        result = emails.stream().filter(e -> e.getEmail().trim().equalsIgnoreCase(email)).findAny().orElseThrow(() -> new RuntimeException("Person not found"));
        cacheDao.put(email, result);
        return result;
    }
}
