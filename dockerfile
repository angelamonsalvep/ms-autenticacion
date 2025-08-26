# Imagen base con Gradle + Java 21
FROM gradle:9.6-jdk21 AS build

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiar archivos de configuración y build.gradle para cachear dependencias
COPY build.gradle settings.gradle gradle.properties ./
COPY infrastructure ./infrastructure
COPY applications ./applications
COPY model ./model
COPY usecase ./usecase

# Descargar dependencias y compilar la app
RUN gradle :app-service:bootJar --no-daemon

# -----------------------
# Stage final: solo jar
# -----------------------
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copiar el jar generado desde el stage de build
COPY --from=build /app/applications/app-service/build/libs/app-service.jar app-service.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "app-service.jar"]
