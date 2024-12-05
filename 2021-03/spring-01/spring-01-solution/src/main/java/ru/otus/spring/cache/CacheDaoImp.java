package ru.otus.spring.cache;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheDaoImp<T> implements CacheDao<T>{
    private final Map<String, T> cache = new HashMap<>();

    @Override
    public boolean isCacheable(String param) {
        return cache.containsKey(param);
    }

    @Override
    public T get(String param) {
        System.out.println("get from chache");
        return cache.get(param);
    }

    @Override
    public void put(String param, T obj) {
        System.out.println("put to chache");
        cache.put(param, obj);
    }
}
