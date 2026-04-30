# Build stage
FROM gradle:jdk26 AS builder
WORKDIR /build
COPY . .
RUN gradle bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:26-jre-alpine
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
