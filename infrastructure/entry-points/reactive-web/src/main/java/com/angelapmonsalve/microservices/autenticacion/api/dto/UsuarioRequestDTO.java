package com.angelapmonsalve.microservices.autenticacion.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Datos necesarios para registrar un usuario")
public record UsuarioRequestDTO(

        @Schema(description = "Nombres del usuario", example = "Juan Carlos")
        @NotBlank(message = "El nombre es obligatorio")
        String nombres,

        @Schema(description = "Apellidos del usuario", example = "Castillo")
        @NotBlank(message = "Los apellidos son obligatorios")
        String apellidos,

        @Schema(description = "Fecha de nacimiento en formato ISO", example = "1990-05-20")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate fechaNacimiento,

        @Schema(description = "Dirección de residencia", example = "Cra 45 # 23-11, Bogotá")
        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @Schema(description = "Teléfono de contacto", example = "3201234567")
        @Pattern(regexp = "\\d{7,10}", message = "El teléfono debe contener entre 7 y 10 dígitos")
        String telefono,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@example.com")
        @Email(message = "Debe ser un correo electrónico válido")
        @NotBlank(message = "El correo electrónico es obligatorio")
        String correoElectronico,

        @Schema(description = "Salario base del usuario", example = "2500000")
        @Min(value = 0, message = "El salario debe ser mayor o igual a 0")
        @Max(value = 15000000, message = "El salario no debe superar 15,000,000")
        Double salarioBase,

        @Schema(description = "Tipo de identificación del usuario", example = "CC")
        @NotBlank(message = "El tipo de identificación es obligatorio")
        String tipoIdentificacion,

        @Schema(description = "Número de identificación del usuario", example = "123456789012345")
        @NotNull(message = "El número de identificación es obligatorio")
        Long numeroIdentificacion
) {}
