FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY services/order-processor/pom.xml services/order-processor/pom.xml
RUN mvn -pl services/order-processor -am dependency:go-offline -B
COPY services/order-processor/src services/order-processor/src
RUN mvn -pl services/order-processor -am package -DskipTests -B

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/order-processor/target/order-processor-1.0.0.jar app.jar
USER 10001
EXPOSE 8081
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]