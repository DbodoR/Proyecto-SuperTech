FROM alpine/java:21-jdk
LABEL authors="juanj"

WORKDIR /app

COPY target/tienda-supertech-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]