package com.angelapmonsalve.microservices.autenticacion.api;

import com.angelapmonsalve.microservices.autenticacion.usecase.autenticacion.AutenticacionUseCase;
import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.UsuarioNoEncontradoException;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.RolNoAutorizadoException;
import com.angelapmonsalve.microservices.autenticacion.api.dto.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoginHandler {
    private final AutenticacionUseCase autenticacionUseCase;

    public Mono<ServerResponse> listenPOSTLoginUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .flatMap(loginRequest -> {
                    Login login = new Login(loginRequest.username(), loginRequest.password(), null); // Ajusta si el DTO tiene rol
                    return autenticacionUseCase.autenticar(login)
                        .flatMap(token -> ServerResponse.ok().bodyValue("{\"token\":\"" + token + "\"}"))
                        .onErrorResume(UsuarioNoEncontradoException.class,
                            e -> ServerResponse.status(401).bodyValue("{\"error\":\"Usuario o clave inválidos\"}"))
                        .onErrorResume(RolNoAutorizadoException.class,
                            e -> ServerResponse.status(403).bodyValue("{\"error\":\"Rol no autorizado\"}"));
                });
    }
}
