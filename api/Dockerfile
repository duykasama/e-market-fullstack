FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
WORKDIR /src
COPY . .
RUN mvn clean package

FROM openjdk:17-jdk-alpine AS final
COPY --from=build /src/target/eMarket-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "eMarket-0.0.1-SNAPSHOT.jar" ]
