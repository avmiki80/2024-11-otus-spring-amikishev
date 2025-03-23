package ru.otus.spring.hw24.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class CustomDatabaseHealthIndicator implements HealthIndicator {
    private final DataSource dataSource;
    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up().withDetail("message", "Database is available").build();
            } else {
                return Health.down().withDetail("message", "Database is unavailable").build();
            }
        } catch (Exception e) {
            return Health.down(e).withDetail("message", "Database connection failed").build();
        }
    }
}
