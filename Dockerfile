# Etapa 1: construir el JAR con Maven
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copiamos todo el proyecto
COPY . .

# Construimos el JAR (sin tests para que sea más rápido)
RUN ./mvnw -B -DskipTests clean package

# Etapa 2: imagen ligera solo con el JAR
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos el JAR generado en la etapa de build
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render asigna el puerto por la variable PORT
ENV PORT=8080

EXPOSE 8080

# Spring Boot leerá server.port=${PORT:8080} de application.yml
ENTRYPOINT ["java","-jar","app.jar"]
