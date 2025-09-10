# ----------- Build Stage -----------
FROM gradle:8.10.2-jdk21-alpine AS build

WORKDIR /app

# Copiar todo el proyecto
COPY . .

# Compilar la app-service y generar el JAR
RUN gradle :app-service:bootJar --no-daemon -x validateStructure

# ----------- Dockerize Stage -----------
FROM alpine:3.18 AS dockerize-stage

RUN apk add --no-cache curl \
    && curl -L https://github.com/jwilder/dockerize/releases/download/v0.9.0/dockerize-linux-amd64-v0.9.0.tar.gz \
    | tar -C /usr/local/bin -xz \
    && chmod +x /usr/local/bin/dockerize

# ----------- Runtime Stage -----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar JAR desde build stage
COPY --from=build /app/applications/app-service/build/libs/*.jar app-service.jar

# Copiar dockerize desde dockerize-stage
COPY --from=dockerize-stage /usr/local/bin/dockerize /usr/local/bin/dockerize

# Exponer el puerto
EXPOSE 8080

# CMD usando dockerize para esperar a que la DB esté lista
CMD ["dockerize", "-wait", "tcp://db:5432", "-timeout", "60s", "java", "-jar", "app-service.jar"]
