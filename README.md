# 📌 Microservicio de Autenticación

## 📝 Introducción

Este microservicio hace parte del **reto del Bootcamp Power Up de Pragma**, y fue generado con el **plugin Scaffold Bancolombia**, garantizando buenas prácticas de diseño, calidad de código y alineación con metodologías de desarrollo modernas.

El microservicio **Autenticación** es responsable de gestionar el registro de usuarios en el sistema.  
Permite crear nuevos usuarios solicitando sus datos personales básicos, garantizando un registro ordenado, validado y persistente.

Este servicio se implementa siguiendo los principios de **arquitectura hexagonal (Clean Architecture)** y aprovechando las capacidades reactivas de **Spring WebFlux** para manejar de forma eficiente las peticiones concurrentes.

---

## 🎯 Historia de Usuario – Registrar usuarios

**HU 1 – Registrar usuarios en el sistema**
- **Como** administrador del sistema
- **Quiero** registrar un nuevo usuario proporcionando sus datos personales básicos (nombres y apellidos separados, fecha de nacimiento, dirección, teléfono, correo electrónico y salario base)
- **Para** mantener un registro claro y ordenado de los clientes potenciales.

### ✅ Criterios de aceptación
- Endpoint disponible: `POST /api/v1/usuarios`
- Validaciones:
    - `nombres`, `apellidos`, `correo_electronico` y `salario_base` no pueden ser nulos ni vacíos.
    - `correo_electronico` debe tener formato válido y no estar previamente registrado.
    - `salario_base` debe ser numérico y estar entre `0` y `15.000.000`.
- La operación de guardado debe ser **transaccional** para garantizar atomicidad.
- Manejo centralizado de excepciones, evitando mensajes inesperados al usuario.
- Trazabilidad mediante **logs con SLF4J**.
- La información registrada debe persistir de forma permanente en la base de datos relacional.

---

## 🏗️ Arquitectura y Tecnologías

- **Lenguaje:** Java 21
- **Framework:** Spring Boot + Spring WebFlux
- **Arquitectura:** Hexagonal (separación de dominio, aplicación e infraestructura)
- **Persistencia:** Base de datos relacional (RDS en AWS en despliegue productivo)
- **Transacciones:** Gestión con `@Transactional`
- **Logs:** Manejo con **SLF4J**
- **Documentación de API:** Swagger/OpenAPI
- **Testing:** JUnit + Mockito (pruebas unitarias)
- **Validación de código:** SonarLint
- **Versionamiento:** GitFlow (una rama por HU)
- **Contenedores:** Docker + AWS ECR
- **Despliegue:** AWS ECS con Fargate y API Gateway

---

## 📊 Logs y Trazabilidad

El sistema implementa trazas y logs mediante **SLF4J**, garantizando una separación entre la API de logging y la implementación subyacente.

### Niveles usados
- `TRACE` → Para trazabilidad fina del flujo.
- `DEBUG` → Para información útil en desarrollo.
- `INFO` → Para hitos importantes (ej: usuario registrado).
- `WARN` → Para situaciones que requieren atención.
- `ERROR` → Para fallos y excepciones manejadas.

### Ejemplo de uso
```java
private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

public Mono<Usuario> registrarUsuario(UsuarioRequest request) {
    log.info("Iniciando registro de usuario con correo: {}", request.getCorreoElectronico());
    return usuarioRepository.save(mapper.toEntity(request))
            .doOnSuccess(u -> log.info("Usuario registrado exitosamente: {}", u.getId()))
            .doOnError(e -> log.error("Error registrando usuario", e));
}
```

---

## 🛠️ Generación con el plugin de Scaffold de Bancolombia

Este proyecto fue **generado con el plugin Clean Architecture de Bancolombia** (scaffold).  
**Versión usada al generar:** `3.24.0`.

### Comando de generación ejecutado
```bash
gradle ca --package=com.angelapmonsalve.microservices.autenticacion --type=reactive --name=ms-autenticacion --lombok=true
```

### Estructura base generada por el plugin
- `domain/model` y `domain/usecase`: núcleo de dominio y casos de uso.
- `applications/app-service`: arranque de la app y configuración de beans.
- `infrastructure/`: punto de entrada y adaptadores.
- Archivos raíz: `build.gradle`, `main.gradle`, `settings.gradle`, `gradle.properties`, `lombok.config`, `deployment/Dockerfile`.

> El plugin sigue el enfoque **Hexagonal** y prepara módulos independientes para favorecer la separación entre **dominio** e **infraestructura**.

---

## ▶️ Ejecución local

### Con Gradle
```bash
./gradlew bootRun
```

### Con Docker
```bash
# Construir la imagen
docker build -t ms-autenticacion .

# Ejecutar el contenedor
docker run -p 8080:8080 ms-autenticacion
```

### Acceso
- API: [http://localhost:8080/api/v1/usuarios](http://localhost:8080/api/v1/usuarios)
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Ejemplo de request JSON
```json
{
  "nombres": "Angela Patricia",
  "apellidos": "Monsalve Paez",
  "fecha_nacimiento": "1990-08-15",
  "direccion": "Calle 123 #45-67",
  "telefono": "3001234567",
  "correo_electronico": "angela.monsalve@example.com",
  "salario_base": 5000000
}
```

### Ejemplo de respuesta exitosa (201 Created)
```json
{
  "id": "1a2b3c4d",
  "nombres": "Angela Patricia",
  "apellidos": "Monsalve Paez",
  "fecha_nacimiento": "1990-08-15",
  "direccion": "Calle 123 #45-67",
  "telefono": "3001234567",
  "correo_electronico": "angela.monsalve@example.com",
  "salario_base": 5000000,
  "fecha_registro": "2025-08-24T15:30:45Z"
}
```
