package ru.otus.spring.hw19.config;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.hw19.domain.Pack;
import ru.otus.spring.hw19.domain.Water;
import ru.otus.spring.hw19.gateway.WaterConverter;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = ConfigTest.class)
@ExtendWith(SpringExtension.class)
class IntegrationConfigTest {

    @Autowired
    private MessageChannel iceChannel;

    @Autowired
    private MessageChannel steamChannel;
    @Autowired
    MessageChannel deliveryChannel;

    @Autowired
    private WaterConverter waterConverter;

    @Test
    public void shouldSendToIceChannel() {
        MessageHandler iceSubscriber = mock(MessageHandler.class);
        ((SubscribableChannel)iceChannel).subscribe(iceSubscriber);

        List<Water> payload = List.of(
                new Water(UUID.randomUUID(), 2, true) // pollutionFactor <= 3 и isToIce = true
        );
        waterConverter.convert(payload);
        ArgumentCaptor<Message<Water>> captor = ArgumentCaptor.forClass(Message.class);

        verify(iceSubscriber, timeout(1000)).handleMessage(captor.capture());
        assertAll(
                () -> assertThat(captor.getValue()).isNotNull(),
                () -> assertThat(captor.getValue().getPayload().getPollutionFactor()).isLessThanOrEqualTo(3)
        );
    }

    @Test
    public void shouldSendToSteamChannel() {
        MessageHandler steamSubscriber = mock(MessageHandler.class);
        ((SubscribableChannel)steamChannel).subscribe(steamSubscriber);
        List<Water> payload = List.of(
                new Water(UUID.randomUUID(), 2, false) // pollutionFactor <= 3 и isToIce = true
        );
        waterConverter.convert(payload);
        ArgumentCaptor<Message<Water>> captor = ArgumentCaptor.forClass(Message.class);

        verify(steamSubscriber, timeout(1000)).handleMessage(captor.capture());
        assertAll(
                () -> assertThat(captor.getValue()).isNotNull(),
                () -> assertThat(captor.getValue().getPayload().getPollutionFactor()).isLessThanOrEqualTo(3)
        );
    }

    @Test
    public void shouldSendToDeliveryChannel() {
        MessageHandler deliverySubscriber = mock(MessageHandler.class);
        ((SubscribableChannel)deliveryChannel).subscribe(deliverySubscriber);
        List<Water> payload = List.of(
                new Water(UUID.randomUUID(), 2, false),
                new Water(UUID.randomUUID(), 4, false),
                new Water(UUID.randomUUID(), 6, false),
                new Water(UUID.randomUUID(), 5, false)
        );
        waterConverter.convert(payload);
        ArgumentCaptor<Message<Pack>> captor = ArgumentCaptor.forClass(Message.class);

        verify(deliverySubscriber, timeout(1000)).handleMessage(captor.capture());
        assertAll(
                () -> assertThat(captor.getValue()).isNotNull(),
                () -> assertThat(captor.getValue().getPayload().objects.size()).isEqualTo(3)
        );
    }

}