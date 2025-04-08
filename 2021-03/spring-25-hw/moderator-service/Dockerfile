FROM maven:3.6.3-openjdk-11-slim AS build

WORKDIR /app
COPY ../pom.xml .
COPY moderator-service/pom.xml ./moderator-service/pom.xml
# костыль, чтоб собрка не падала из-за не найденого модуля
COPY book-service/pom.xml ./book-service/pom.xml
RUN mvn dependency:resolve

COPY moderator-service/src ./moderator-service/src
RUN mvn clean install -pl moderator-service -am -Dmaven.test.skip=true -DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/moderator-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]