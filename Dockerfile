FROM maven:3.8.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests



# openjdk image with jdk version <24 come with ssh vulnerability.
FROM openjdk:24-jdk-slim

COPY --from=build /app/target/userservice-*.jar app.jar
COPY --from=build /app/src/main/resources/provadis_logo.jpeg resources/provadis_logo.jpeg

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]
