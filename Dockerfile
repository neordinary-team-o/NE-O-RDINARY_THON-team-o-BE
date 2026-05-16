FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar --no-daemon
RUN JAR_FILE=$(find build/libs -name "*.jar" ! -name "*plain.jar" | head -n 1) && cp "$JAR_FILE" app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
