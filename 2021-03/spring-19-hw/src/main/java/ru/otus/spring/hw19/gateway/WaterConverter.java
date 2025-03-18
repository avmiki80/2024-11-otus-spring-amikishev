package ru.otus.spring.hw19.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.hw19.domain.Water;

import java.util.Collection;

@MessagingGateway()
public interface WaterConverter {
    @Gateway(requestChannel = "waterChannel", headers = @GatewayHeader(name = "waterfall", value = "water"))
    void convert(Collection<Water> waterFall);
}
