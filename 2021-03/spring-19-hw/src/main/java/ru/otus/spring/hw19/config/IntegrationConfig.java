package ru.otus.spring.hw19.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.*;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import ru.otus.spring.hw19.domain.Ice;
import ru.otus.spring.hw19.domain.Steam;
import ru.otus.spring.hw19.domain.Water;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Configuration
@IntegrationComponentScan(basePackages = "ru.otus.spring.hw19.gateway")
public class IntegrationConfig {
    private final static int COUNT_THREAD = 5;
    @Bean
    public IntegrationFlow waterFlow(){
        return IntegrationFlows.from("waterChannel")
                .log(LoggingHandler.Level.INFO, "waterFlow", m -> "Обрабатываем воду: " + m.getPayload())
                .split()
                .channel("cleanChannel")
                .handle("cleanWaterServiceImpl", "water")
                .<Water>filter(f -> f.getPollutionFactor() <= 3)
                .<Water, Boolean>route(
                        Water::isToIce,
                        mapping -> mapping
                                .subFlowMapping(true, sf -> sf.channel("iceChannel"))
                                .subFlowMapping(false, sf -> sf.channel("steamChannel"))
                )
                .get();
    }
    @Bean
    public IntegrationFlow steamFlow(){
        return IntegrationFlows.from("steamChannel")
                .<Water, Steam>transform(w -> {
                    System.out.println("Вода нагревается:" + w);
                    return new Steam(w.getUuid(), w.getPollutionFactor(), 10);
                })
                .aggregate(this::configureAggregator)
                .handle("steamPackageService", "packaging")
                .channel("deliveryChannel")
                .get();
    }
    @Bean
    public IntegrationFlow iceFlow(){
        return IntegrationFlows.from("iceChannel")
                .<Water, Ice>transform(w -> {
                    System.out.println("Вода замораживается:" + w);
                    return new Ice(w.getUuid(), w.getPollutionFactor(), 1);
                })
                .aggregate(this::configureAggregator)
                .handle("icePackageService", "packaging")
                .channel("deliveryChannel")
                .get();
    }
    private <T> void configureAggregator(AggregatorSpec aggregator) {
        aggregator
                .correlationStrategy(message -> message.getHeaders().get("waterfall"))
                .releaseStrategy(group -> group.size() == 3)
                .expireGroupsUponCompletion(true)
                .outputProcessor(group -> {
                    List<T> items = group.getMessages()
                            .stream()
                            .map(message -> (T) message.getPayload())
                            .collect(Collectors.toList());
                    return MessageBuilder.withPayload(items).build();
                });
    }
    @Bean
    public IntegrationFlow deliveryFlow(){
        return IntegrationFlows.from("deliveryChannel")
                .handle(message -> System.out.println("Доставка: " + message.getPayload()))
                .get();
    }
    @Bean
    public MessageChannel waterChannel(){
        return new QueueChannel(100);
    }

    @Bean
    public MessageChannel cleanChannel(){
        return MessageChannels.publishSubscribe(Executors.newFixedThreadPool(COUNT_THREAD)).minSubscribers(0).get();
    }
    @Bean
    public MessageChannel steamChannel(){
        return createPublishSubscribeChannel();
    }
    @Bean
    public MessageChannel iceChannel(){
        return createPublishSubscribeChannel();
    }
    @Bean
    public MessageChannel deliveryChannel(){
        return createPublishSubscribeChannel();
    }

    private MessageChannel createPublishSubscribeChannel() {
        return MessageChannels.publishSubscribe().minSubscribers(0).get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers
                .fixedRate( 100 )
                .maxMessagesPerPoll( 2 )
                .taskExecutor(Executors.newFixedThreadPool(COUNT_THREAD))
                .get();
    }
}
