FROM maven:3.6.3-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim
WORKDIR /app
COPY --from=build /app/target/spring-24-hw-0.0.2.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]