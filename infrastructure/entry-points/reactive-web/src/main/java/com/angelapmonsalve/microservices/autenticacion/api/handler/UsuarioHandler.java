package com.angelapmonsalve.microservices.autenticacion.api.handler;

import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.api.mapper.UsuarioMapper;
import com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario.ConsultarExistenciaUsuarioUseCase;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.exception.UsuarioInvalidoException;
import com.angelapmonsalve.microservices.autenticacion.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UsuarioHandler {
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
    private final ConsultarExistenciaUsuarioUseCase consultarExistenciaUsuarioUseCase;
    private final Validator validator;
    private final JwtTokenProvider jwtTokenProvider;

    public Mono<ServerResponse> registrarUsuario(ServerRequest request) {
        String authHeader = request.headers().firstHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("No se encontró el token de autenticación");
        }
        String rol;
        try {
            rol = jwtTokenProvider.getRolFromToken(authHeader);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Token inválido o expirado");
        }
        if (!"admin".equalsIgnoreCase(rol) && !"asesor".equalsIgnoreCase(rol)) {
            return ServerResponse.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("No tienes permisos para registrar usuarios");
        }
        return request.bodyToMono(UsuarioRequestDTO.class)
                .flatMap(dto -> {
                    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dto, UsuarioRequestDTO.class.getName());
                    validator.validate(dto, errors);
                    if (errors.hasErrors()) {
                        String errorMsg = errors.getAllErrors().stream()
                            .map(org.springframework.validation.ObjectError::getDefaultMessage)
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("Datos inválidos");
                        return Mono.error(new UsuarioInvalidoException(errorMsg));
                    }
                    return registrarUsuarioUseCase.registrarUsuario(UsuarioMapper.toDomain(dto))
                            .map(UsuarioMapper::toResponseDTO)
                            .flatMap(responseDTO -> ServerResponse.status(201)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(responseDTO));
                });
    }

    public Mono<ServerResponse> consultarExistenciaUsuario(ServerRequest request) {
        String tipoIdentificacion = request.queryParam("tipoIdentificacion").orElse("");
        String numeroIdentificacionStr = request.queryParam("numeroIdentificacion").orElse("");
        if (tipoIdentificacion.isEmpty() || numeroIdentificacionStr.isEmpty()) {
            return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("Faltan parámetros tipoIdentificacion o numeroIdentificacion");
        }
        long numeroIdentificacion;
        try {
            numeroIdentificacion = Long.parseLong(numeroIdentificacionStr);
        } catch (NumberFormatException e) {
            return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("numeroIdentificacion debe ser un número válido");
        }
        return consultarExistenciaUsuarioUseCase.existePorTipoYNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion)
            .flatMap(existe -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(existe));
    }
}
