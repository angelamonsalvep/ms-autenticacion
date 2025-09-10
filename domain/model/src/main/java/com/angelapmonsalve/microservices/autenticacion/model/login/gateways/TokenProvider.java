package com.angelapmonsalve.microservices.autenticacion.model.login.gateways;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import reactor.core.publisher.Mono;

public interface TokenProvider {
    Mono<String> generarToken(Login login);
    // Puedes agregar métodos para validar el token si lo necesitas
}

