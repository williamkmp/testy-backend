FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw install -DskipTests

FROM eclipse-temurin:21-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY --from=builder ./app/target/sectask-0.0.1.jar app.jar
EXPOSE 5000
ENTRYPOINT [ "java", "-jar", "/app.jar" ]z