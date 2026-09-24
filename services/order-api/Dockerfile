FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY services/order-api/pom.xml services/order-api/pom.xml
RUN mvn -pl services/order-api -am dependency:go-offline -B
COPY services/order-api/src services/order-api/src
RUN mvn -pl services/order-api -am package -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/order-api/target/order-api-1.0.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]