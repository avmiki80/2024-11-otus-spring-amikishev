package ru.otus.spring.service.examing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceServiceConfigTest;
import ru.otus.spring.exception.CustomServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = ServiceServiceConfigTest.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Класс RateServiceImpl")
class RateServiceImplTest {
    @Autowired
    private RateService rateService;
    @Autowired
    private RateService wrongRateService;

    @DisplayName("Проверка поднятия контекста")
    @Test
    public void checkContext(){
        assertAll(
                () -> assertThat(rateService).isNotNull(),
                () -> assertThat(wrongRateService).isNotNull()
        );
    }
    @DisplayName("Проверка результата, если количество правильных ответов меньше порогового")
    @Test
    public void whenLessThresholdCorrectAnswer_thenTryAgain(){
        assertThat(rateService.rate(2L)).isEqualTo("Попробуй еще");
    }
    @DisplayName("Проверка результата, если количество правильных ответов больше или равно порогового")
    @Test
    public void whenAboveOrEqualThresholdCorrectAnswer_thenTryAgain(){
        assertThat(rateService.rate(3L)).isEqualTo("Поздравляем");
    }
    @DisplayName("Проверка выброса исключения если не задано пороговое значение")
    @Test
    public void whenNullThresholdCorrectAnswer_thenDoesThrowException(){
        assertAll(
                () -> {
                    CustomServiceException exception = assertThrows(CustomServiceException.class, () -> wrongRateService.rate(0L));
                    assertThat(exception.getMessage()).isEqualTo("Не установлено кррректное значение порога правльных ответов");
                }
        );
    }
}