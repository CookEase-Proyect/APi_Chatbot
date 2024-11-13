
FROM maven:3.8.4-openjdk-17-slim AS build

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar los archivos necesarios al contenedor
COPY pom.xml .
COPY src ./src

# Compilar el proyecto y construir el archivo JAR
RUN mvn clean package -DskipTests

# Segunda etapa para ejecutar el JAR en un contenedor más ligero
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR desde la fase de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto que usa la aplicación (30343 según tu configuración)
EXPOSE 8081

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]