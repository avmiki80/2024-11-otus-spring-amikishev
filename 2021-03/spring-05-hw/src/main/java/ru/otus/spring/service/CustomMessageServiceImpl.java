package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomMessageServiceImpl implements CustomMessageService {
    private final MessageSource messageSource;
    @Override
    public String getMessage(String bundleKey) {
        return messageSource.getMessage(bundleKey, null, LocaleContextHolder.getLocale());
    }
}
