package ru.otus.spring.hw19.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.hw19.domain.Water;
import ru.otus.spring.hw19.gateway.WaterConverter;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomBusinessService implements BusinessService {
    private final WaterConverter waterConverter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SneakyThrows
    @Override
    public void customLogic() {

        while (true) {
            Thread.sleep(4000);
            executorService.execute(() -> {
                Collection<Water> items = generateWaterFall();
                System.out.println("waters: " +
                        items.stream()
                                .map(w -> w.getUuid() + " :" + w.getPollutionFactor())
                                .collect(Collectors.joining(",")));
                waterConverter.convert(items);
            });
        }
    }

    private Collection<Water> generateWaterFall() {
        List<Water> waterFall = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(3, 8); i++) {
            waterFall.add(generateWater());
        }
        return waterFall;
    }

    private Water generateWater() {
        return new Water(UUID.randomUUID(), RandomUtils.nextInt(1, 10), RandomUtils.nextBoolean());
    }
    @PreDestroy
    void destroy(){
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
