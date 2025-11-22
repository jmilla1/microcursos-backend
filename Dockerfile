# Etapa 1: construir el JAR con Maven
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos pom y código
COPY pom.xml .
COPY src ./src

# Compilamos sin tests
RUN mvn -q -DskipTests package

# Etapa 2: imagen liviana solo con el JAR
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Render expone un PORT; Spring Boot debe escuchar en ese puerto
ENV PORT=8080
EXPOSE 8080

# Usamos el valor de $PORT que Render setea
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
