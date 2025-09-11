package com.angelapmonsalve.microservices.autenticacion.usecase.autenticacion;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.LoginRepository;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.TokenProvider;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.UsuarioNoEncontradoException;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.RolNoAutorizadoException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase {
    private final LoginRepository loginRepository;
    private final TokenProvider tokenProvider;

    public Mono<String> autenticar(Login login) {
        return loginRepository.findByCorreo(login.getCorreo())
            .switchIfEmpty(Mono.error(new UsuarioNoEncontradoException()))
            .flatMap(usuario -> {
                if (!usuario.getClave().equals(login.getClave())) {
                    return Mono.error(new UsuarioNoEncontradoException());
                }
                if (login.getRol() != null && usuario.getRol() != login.getRol()) {
                    return Mono.error(new RolNoAutorizadoException());
                }
                return tokenProvider.generarToken(usuario);
            });
    }
}
