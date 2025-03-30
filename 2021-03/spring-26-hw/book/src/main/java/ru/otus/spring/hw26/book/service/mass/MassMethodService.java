package ru.otus.spring.hw26.book.service.mass;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface MassMethodService<D> {
    List<D> massCreate(List<D> objs);
    List<D> massCreate1(List<D> objs);
    List<D> massCreate2(List<D> objs);
    List<D> massUpdate(List<D> objs);
    void massDelete(List<Long> ids);

//    CompletableFuture<List<D>> massCreate3(List<D> objs);
//    CompletableFuture<List<D>> massCreate4(List<D> objs);
//    CompletableFuture<List<D>> massCreate5(List<D> objs);
}
