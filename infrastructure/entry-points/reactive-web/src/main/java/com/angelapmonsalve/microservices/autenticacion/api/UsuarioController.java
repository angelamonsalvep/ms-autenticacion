package com.angelapmonsalve.microservices.autenticacion.api;

import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioResponseDTO;
import com.angelapmonsalve.microservices.autenticacion.api.mapper.UsuarioMapper;
import com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario.ConsultarExistenciaUsuarioUseCase;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ConsultarExistenciaUsuarioUseCase consultarExistenciaUsuarioUseCase;

    @Operation(summary = "Registrar un nuevo solicitante")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario registrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo exitoso",
                                            summary = "Registro correcto",
                                            value = """
                        {
                          "id": "123",
                          "nombres": "Gael",
                          "apellidos": "Castillo",
                          "fechaNacimiento": "1995-05-15",
                          "correoElectronico": "gael.castillo@mail.com",
                          "telefono": "3001234567",
                          "direccion": "Calle Falsa 123",
                          "salarioBase": 3500000,
                          "tipoIdentificacion": "CC",
                          "numeroIdentificacion": 1234567890
                        }
                        """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error de validación en los datos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Falta nombres",
                                            summary = "Campo nombres vacío",
                                            value = """
                        {
                          "codigo": "VALIDATION_ERROR",
                          "mensaje": "El campo 'nombres' es obligatorio."
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Falta apellidos",
                                            summary = "Campo apellidos vacío",
                                            value = """
                        {
                          "codigo": "VALIDATION_ERROR",
                          "mensaje": "El campo 'apellidos' es obligatorio."
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Correo duplicado",
                                            summary = "El correo ya está registrado",
                                            value = """
                        {
                          "codigo": "DUPLICATE_EMAIL",
                          "mensaje": "El correo gael.castillo@mail.com ya está registrado."
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Salario inválido",
                                            summary = "El salario base no cumple el rango",
                                            value = """
                        {
                          "codigo": "INVALID_SALARY",
                          "mensaje": "El salario base debe estar entre 0 y 15000000."
                        }
                        """
                                    ),
                                    @ExampleObject(
                                            name = "Documento duplicado",
                                            summary = "Ya existe un usuario con ese tipo y número de identificación",
                                            value = """
                        {
                          "codigo": "DUPLICATE_DOCUMENT",
                          "mensaje": "Ya existe un usuario con ese tipo y número de identificación."
                        }
                        """
                                    )
                            }
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = UsuarioRequestDTO.class),
            examples = {
                @ExampleObject(
                    name = "Registro válido",
                    summary = "Ejemplo de registro correcto (modifica el correo para evitar duplicados)",
                    value = """
                    {
                      "nombres": "Gael",
                      "apellidos": "Castillo",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "gael.castillo+unico@mail.com",
                      "salarioBase": 3500000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                ),
                @ExampleObject(
                    name = "Falta nombres",
                    summary = "Campo nombres vacío",
                    value = """
                    {
                      "nombres": "",
                      "apellidos": "Castillo",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "gael.castillo@mail.com",
                      "salarioBase": 3500000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                ),
                @ExampleObject(
                    name = "Falta apellidos",
                    summary = "Campo apellidos vacío",
                    value = """
                    {
                      "nombres": "Gael",
                      "apellidos": "",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "gael.castillo@mail.com",
                      "salarioBase": 3500000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                ),
                @ExampleObject(
                    name = "Correo duplicado",
                    summary = "El correo ya está registrado",
                    value = """
                    {
                      "nombres": "Gael",
                      "apellidos": "Castillo",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "correo.duplicado@mail.com",
                      "salarioBase": 3500000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                ),
                @ExampleObject(
                    name = "Salario inválido",
                    summary = "El salario base no cumple el rango",
                    value = """
                    {
                      "nombres": "Gael",
                      "apellidos": "Castillo",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "gael.castillo@mail.com",
                      "salarioBase": -1000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                ),
                @ExampleObject(
                    name = "Documento duplicado",
                    summary = "Ya existe un usuario con ese tipo y número de identificación",
                    value = """
                    {
                      "nombres": "Gael",
                      "apellidos": "Castillo",
                      "fechaNacimiento": "1995-05-15",
                      "direccion": "Calle Falsa 123",
                      "telefono": "3001234567",
                      "correoElectronico": "gael.castillo@mail.com",
                      "salarioBase": 3500000,
                      "tipoIdentificacion": "CC",
                      "numeroIdentificacion": 1234567890
                    }
                    """
                )
            }
        )
    )
    @PostMapping
    public Mono<ResponseEntity<UsuarioResponseDTO>> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        return registrarUsuarioUseCase.registrarUsuario(UsuarioMapper.toDomain(request))
                .map(UsuarioMapper::toResponseDTO)
                .map(dto -> ResponseEntity.status(201).body(dto));
    }

    @Operation(
        summary = "Verifica si existe un usuario por tipo y número de identificación",
        description = "Devuelve true si existe un usuario con el tipo y número de identificación proporcionados, false en caso contrario.",
        parameters = {
            @io.swagger.v3.oas.annotations.Parameter(
                name = "tipoIdentificacion",
                description = "Tipo de identificación (por ejemplo, CC, NIT, PASAPORTE)",
                required = true,
                example = "CC"
            ),
            @io.swagger.v3.oas.annotations.Parameter(
                name = "numeroIdentificacion",
                description = "Número de identificación",
                required = true,
                example = "1234567890"
            )
        },
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Resultado de la verificación",
                content = @Content(
                    mediaType = "application/json",
                    examples = {
                        @ExampleObject(
                            name = "Existe usuario",
                            summary = "El usuario existe",
                            value = "true"
                        ),
                        @ExampleObject(
                            name = "No existe usuario",
                            summary = "El usuario no existe",
                            value = "false"
                        )
                    }
                )
            )
        }
    )
    @GetMapping("/existe")
    public Mono<ResponseEntity<Boolean>> existeUsuarioPorDocumento(
            @RequestParam("tipoIdentificacion") String tipoIdentificacion,
            @RequestParam("numeroIdentificacion") Long numeroIdentificacion) {
        return consultarExistenciaUsuarioUseCase.existePorTipoYNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion)
                .map(ResponseEntity::ok);
    }
}
