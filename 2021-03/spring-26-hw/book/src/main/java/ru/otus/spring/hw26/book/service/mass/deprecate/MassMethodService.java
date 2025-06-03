package ru.otus.spring.hw26.book.service.mass.deprecate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
@Deprecated
public interface MassMethodService<D> {
    List<D> massCreate(List<D> objs);
    List<D> massCreate1(List<D> objs);
    List<D> massCreate2(List<D> objs);
    List<D> massUpdate(List<D> objs);
    void massDelete(List<Long> ids);
}
