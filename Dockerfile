FROM maven:4.0.0-openjdk-25 AS build
LABEL authors="juanj"

WORKDIR /app

COPY pom.xml /

RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN ls -la /app/target/
FROM openjdk:25-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/tienda-supertech-0.0.1-SNAPSHOT.jar /app/tienda-supertech.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/tienda-supertech.jar"]