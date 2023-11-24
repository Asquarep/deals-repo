FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/clusteredDataWarehouse-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "/app/clusteredDataWarehouse-0.0.1-SNAPSHOT.jar" ]