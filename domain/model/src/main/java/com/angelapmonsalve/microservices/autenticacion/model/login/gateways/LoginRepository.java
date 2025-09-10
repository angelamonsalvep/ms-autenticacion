package com.angelapmonsalve.microservices.autenticacion.model.login.gateways;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import reactor.core.publisher.Mono;

public interface LoginRepository {
    Mono<Login> findByCorreo(String correo);
    Mono<String> generarToken(Login login);
}
