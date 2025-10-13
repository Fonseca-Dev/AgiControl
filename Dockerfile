# Etapa 1 - Build
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

# Etapa 2 - Imagem final
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/Sistema-Gastos-Review-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
