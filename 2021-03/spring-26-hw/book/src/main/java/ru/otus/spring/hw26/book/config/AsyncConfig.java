package ru.otus.spring.hw26.book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AsyncConfig {
    private final int hikariMaximumPoolSize;
    private final int maxThreadPoolSize;
    private final int threadPoolSize;

    public AsyncConfig(@Value("${spring.datasource.hikari.maximum-pool-size}") int hikariMaximumPoolSize) {
        this.hikariMaximumPoolSize = hikariMaximumPoolSize;
        // оставляем минимум одно соединение для не массовых методов.
        this.maxThreadPoolSize = hikariMaximumPoolSize - 1;
        // делим полученное выше значение на количество массовых методов
        this.threadPoolSize = maxThreadPoolSize / 3;
    }

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolSize);
        executor.setMaxPoolSize(maxThreadPoolSize);
        executor.setQueueCapacity(50000);
        executor.setThreadNamePrefix("db-");
        executor.initialize();
        return executor;
    }
    @Bean(name = "databaseExecutor")
    public ExecutorService databaseExecutor() {
//        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        return Executors.newFixedThreadPool(threadPoolSize);
    }
    @PreDestroy
    public void destroy() {
        ExecutorService executor = databaseExecutor();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
