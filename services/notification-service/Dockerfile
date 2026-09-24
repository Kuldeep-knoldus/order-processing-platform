FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY services/notification-service/pom.xml services/notification-service/pom.xml
RUN mvn -pl services/notification-service -am dependency:go-offline -B
COPY services/notification-service/src services/notification-service/src
RUN mvn -pl services/notification-service -am package -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/notification-service/target/notification-service-1.0.0.jar app.jar
USER 10001
EXPOSE 8082
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]