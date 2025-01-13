package ru.otus.spring.service.examing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.config.ServiceConfigTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.spring.exception.ServiceException;
import ru.otus.spring.service.examing.RateService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.spring.message.ServiceMessage.*;

@ContextConfiguration(classes = ServiceConfigTest.class)
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
        assertThat(rateService.rate(2L)).isEqualTo(TRY_IT_AGAIN);
    }
    @DisplayName("Проверка результата, если количество правильных ответов больше или равно порогового")
    @Test
    public void whenAboveOrEqualThresholdCorrectAnswer_thenTryAgain(){
        assertThat(rateService.rate(3L)).isEqualTo(CONGRATULATION);
    }
    @DisplayName("Проверка выброса исключения если не задано пороговое значение")
    @Test
    public void whenNullThresholdCorrectAnswer_thenDoesThrowException(){
        assertAll(
                () -> {
                    ServiceException exception = assertThrows(ServiceException.class, () -> wrongRateService.rate(0L));
                    assertThat(exception.getMessage()).isEqualTo(WRONG_THRESHOLD_CORRECT_ANSWER);
                }
        );
    }
}