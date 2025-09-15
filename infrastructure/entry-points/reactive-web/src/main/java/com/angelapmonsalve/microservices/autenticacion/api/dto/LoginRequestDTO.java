package com.angelapmonsalve.microservices.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para login de usuario")
public record LoginRequestDTO(
    @Schema(description = "Usuario o correo", example = "usuario@example.com")
    @NotBlank(message = "El usuario es obligatorio")
    String username,

    @Schema(description = "Contraseña", example = "password123")
    @NotBlank(message = "La contraseña es obligatoria")
    String password
) {}

