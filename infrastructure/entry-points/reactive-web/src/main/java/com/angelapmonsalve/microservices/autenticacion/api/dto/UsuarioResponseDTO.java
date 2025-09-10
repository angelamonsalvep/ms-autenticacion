package com.angelapmonsalve.microservices.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record UsuarioResponseDTO(

        @Schema(
                description = "Identificador único del usuario",
                example = "123"
        )
        String id,

        @Schema(
                description = "Nombres del solicitante",
                example = "Gael"
        )
        String nombres,

        @Schema(
                description = "Apellidos del solicitante",
                example = "Castillo"
        )
        String apellidos,

        @Schema(
                description = "Fecha de nacimiento en formato ISO",
                example = "1995-05-15"
        )
        LocalDate fechaNacimiento,

        @Schema(
                description = "Correo electrónico único y válido",
                example = "gael.castillo@mail.com"
        )
        String correoElectronico,

        @Schema(
                description = "Número de teléfono de contacto",
                example = "3001234567"
        )
        String telefono,

        @Schema(
                description = "Dirección de residencia",
                example = "Calle Falsa 123"
        )
        String direccion,

        @Schema(
                description = "Salario base del solicitante (entre 0 y 15'000.000)",
                example = "3500000"
        )
        Double salarioBase,

        @Schema(
                description = "Tipo de identificación del usuario",
                example = "CC"
        )
        String tipoIdentificacion,

        @Schema(
                description = "Número de identificación del usuario",
                example = "123456789012345"
        )
        Long numeroIdentificacion
) {}
