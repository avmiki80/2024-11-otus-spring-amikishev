package ru.otus.spring.hw26.book.service.mass.deprecate;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw26.book.dto.CommentDto;
import ru.otus.spring.hw26.book.service.mass.BatchService;
import ru.otus.spring.hw26.book.service.mass.CommentBatchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;


import static ru.otus.spring.hw26.book.util.Utils.splitToBatches;

//@Service
@Deprecated
public class MassMethodComment implements MassMethodService<CommentDto> {
    private final BatchService<CommentDto> commentBatchService;
    private final ExecutorService databaseExecutor;
    private final Executor taskExecutor;
    private final int batchSize;
    public MassMethodComment(
            CommentBatchService commentBatchService, ExecutorService databaseExecutor, Executor taskExecutor,
            @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}") int batchSize) {
        this.commentBatchService = commentBatchService;
        this.databaseExecutor = databaseExecutor;
        this.taskExecutor = taskExecutor;
        this.batchSize = batchSize;
    }

// Не пинайте сильно ногами за попытку разобраться с массовыми методами. Лучше дайте обратную связь как делать правильно.

// Метод разделяет входящюу коллекцию на пачки и сохраняет каждую. Обработка ошибок в методах BatchService.
// Если давать увеливающуюся нагрузку, то в grafana видно как начинает увеличиваться количество потоков ожидающих
// освобождения соединений hikari pool. И после достижения критической массы, выбрасывается исключение по connectionTimeout
    @Override
    public List<CommentDto> massCreate(List<CommentDto> objs) {
        List<CommentDto> result = new ArrayList<>(objs.size());
        splitToBatches(objs, batchSize).forEach(b -> result.addAll(commentBatchService.batchSave(b)));
        return result;
    }

    // Метод разделяет входящюу коллекцию на пачки и сохраняет каждую. Обработка ошибок в методах BatchService.
    // Попытка обуздать некотролируемы рост потоков ожидающих освобождения соединений hikari pool.
    // Использовать FixedThreadExecutor. Работает ограниченное число потоков. Остальные ждут свою очередь.
    @SneakyThrows
    @Override
    public List<CommentDto> massCreate1(List<CommentDto> objs) {
        Collection<List<CommentDto>> batches = splitToBatches(objs, batchSize);

        List<CompletableFuture<List<CommentDto>>> batchFutures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(
                        () -> commentBatchService.batchSave(batch),
                        databaseExecutor
                ))
                .collect(Collectors.toList());

        // Объединяем результаты всех батчей
        return CompletableFuture.allOf(batchFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> batchFutures.stream()
                        .map(CompletableFuture::join)
                        .flatMap(List::stream)
                        .collect(Collectors.toList())).get(180, TimeUnit.SECONDS);
    }

    // Тоже самое, что предыдущий метод только с использованием ThreadPoolTaskExecutor
    @SneakyThrows
    @Override
    public List<CommentDto> massCreate2(List<CommentDto> objs) {
        Collection<List<CommentDto>> batches = splitToBatches(objs, batchSize);

        // Создаем список futures для каждого батча
        List<CompletableFuture<List<CommentDto>>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> commentBatchService.batchSave(batch), taskExecutor))
                .collect(Collectors.toList());

        // Комбинируем все futures в один
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList())).get(180, TimeUnit.SECONDS);
    }

    @SneakyThrows
    @Override
    public List<CommentDto> massUpdate(List<CommentDto> objs) {
        Collection<List<CommentDto>> batches = splitToBatches(objs, batchSize);

        List<CompletableFuture<List<CommentDto>>> futures = batches.stream()
                .map(batch -> CompletableFuture.supplyAsync(() -> commentBatchService.batchUpdate(batch), taskExecutor))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .flatMap(future -> future.join().stream())
                        .collect(Collectors.toList())).get(180, TimeUnit.SECONDS);
    }

    @SneakyThrows
    @Override
    public void massDelete(List<Long> ids) {
        Collection<List<Long>> batches = splitToBatches(ids, batchSize);

        List<CompletableFuture<Void>> futures = batches.stream()
                .map(batch -> CompletableFuture.runAsync(() -> commentBatchService.batchDelete(batch), taskExecutor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .get(180, TimeUnit.SECONDS);
    }
}
