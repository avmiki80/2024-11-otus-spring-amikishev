FROM maven:3.6.3-openjdk-11-slim AS build

WORKDIR /app
COPY ../pom.xml .
COPY book-service/pom.xml ./book-service/pom.xml
# костыль, чтоб собрка не падала из-за не найденого модуля
COPY moderator-service/pom.xml ./moderator-service/pom.xml

RUN mvn dependency:resolve

COPY book-service/src ./book-service/src
RUN mvn clean install -pl book-service -am -Dmaven.test.skip=true-DskipTests

FROM openjdk:11-jdk-slim

WORKDIR /app

COPY --from=build /app/book-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]