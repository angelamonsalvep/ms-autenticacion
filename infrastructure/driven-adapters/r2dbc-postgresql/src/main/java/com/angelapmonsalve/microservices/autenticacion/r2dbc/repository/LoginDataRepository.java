package com.angelapmonsalve.microservices.autenticacion.r2dbc.repository;

import com.angelapmonsalve.microservices.autenticacion.r2dbc.entities.LoginData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LoginDataRepository extends ReactiveCrudRepository<LoginData, String> {
    Mono<LoginData> findByCorreo(String correo);
}

