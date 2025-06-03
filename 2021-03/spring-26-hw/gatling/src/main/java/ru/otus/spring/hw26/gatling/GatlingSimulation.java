package ru.otus.spring.hw26.gatling;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;


public class GatlingSimulation extends Simulation {

    // 1. Определение HTTP протокола
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .contentTypeHeader("application/json")
            .authorizationHeader("Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sImV4dCI6MjAxNjIzOTAyMiwiaWF0IjoxNTE2MjM5MDIyfQ.QqUWw8a3AbmX6lRg7xl2Ks3WMk7ORurS-M-qjiiTTdDB-zOK7_-RRTavgPBXNHjq5jVsC6U29fY6RRFh3YP3SA");

    // Генерация JSON-массива коментариев
    ChainBuilder generateComments =
            exec(session -> {
                String commentsJson = IntStream.rangeClosed(1, 1000)
                        .mapToObj(i -> String.format(
                                "{\"text\":\"Comment%d\",\"bookTitle\":\"Book%d\"}", i, i%3))
                        .collect(Collectors.joining(",", "[", "]"));
                return session.set("commentsJson", commentsJson);
            });

    // 2. Определение сценария
    ScenarioBuilder comments =
            scenario("Mass create comments loading")
                    .exec(generateComments)
//                    .exec(
//                            http("Find comment by id")
//                                    .get("/comment/1")
//                                    .header("content-type", "application/json; charset=utf-8")
//                                    .requestTimeout(1200))
                    .exec(
                            http("Mass create comments")
                                    .post("/comments")
                                    .header("content-type", "application/json; charset=utf-8")
                                    .requestTimeout(1200)
                                    .body(StringBody("#{commentsJson}")));
//                    .exec(
//                            http("Find comment by id")
//                                    .get("/comment/2")
//                                    .header("content-type", "application/json; charset=utf-8")
//                                    .requestTimeout(1200));

    // 3. Настройка нагрузки
    {
        setUp(
                comments.injectOpen(
                        nothingFor(5), // Ничего не делать 5 секунд
                        rampUsers(5).during(10), // Линейно увеличивать до 5 запросов за 10 секунд
                        constantUsersPerSec(5).during(30), // Поддерживать 10 запросов/сек в течение 30 секунд
                        rampUsersPerSec(5).to(20).during(20) // Увеличивать с 1 до 5 запросов/сек за 20 секунд
                )
        ).protocols(httpProtocol)
                .maxDuration(Duration.ofMinutes(5));
    }
}
