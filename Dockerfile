FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY target/bank-service-*.jar app.jar

WORKDIR /app/build
EXPOSE 8080
ENTRYPOINT java -jar service.jar
