FROM maven:3.9.9-eclipse-temurin-21-alpine  AS build

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM  openjdk:26-ea-21-jdk-oracle

WORKDIR /app

COPY --from=build /target/*.jar api-gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]