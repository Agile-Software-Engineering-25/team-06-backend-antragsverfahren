FROM maven:3.8.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests



# openjdk image with jdk version <24 come with ssh vulnerability.
FROM openjdk:21-jdk

COPY --from=build /app/target/userservice-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
