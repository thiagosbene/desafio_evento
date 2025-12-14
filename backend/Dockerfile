# === Etapa 1: Build ===
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Compila e empacota o JAR
RUN mvn clean package -DskipTests

# === Etapa 2: Runtime ===
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o JAR do stage anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080
EXPOSE 8080

# Inicia a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]