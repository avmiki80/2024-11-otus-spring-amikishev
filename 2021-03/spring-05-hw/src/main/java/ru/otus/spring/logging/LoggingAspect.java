package ru.otus.spring.logging;

import lombok.extern.java.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

@Component
@Aspect
@Log
public class LoggingAspect {
    @Around("execution(* ru.otus.spring.dao.*.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Класс : " + joinPoint.getTarget().getClass().getName());
        log.info("Метод : " + joinPoint.getSignature().toShortString());
        if(Objects.nonNull(joinPoint.getArgs()))
            Arrays.stream(joinPoint.getArgs()).forEach(i ->log.info("Параметры : " + i.toString()));

        Object result = joinPoint.proceed();

        log.info("Результат метода : " + result.toString());
        return result;
    }
}
