FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

COPY gradlew .
COPY gradle gradle/
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .

COPY src src/

RUN chmod +x gradlew

RUN ./gradlew clean shadowJar --stacktrace --info




FROM eclipse-temurin:17-jre-jammy AS cli

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-XX:InitialRAMPercentage=70.0", "-XX:MaxRAMPercentage=70.0", "-jar", "app.jar"]

CMD []
