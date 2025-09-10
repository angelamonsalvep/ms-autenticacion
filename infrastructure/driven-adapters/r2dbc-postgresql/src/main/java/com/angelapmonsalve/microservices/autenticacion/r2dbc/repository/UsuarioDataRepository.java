package com.angelapmonsalve.microservices.autenticacion.r2dbc.repository;

import com.angelapmonsalve.microservices.autenticacion.r2dbc.entities.UsuarioData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioDataRepository  extends ReactiveCrudRepository<UsuarioData, String>, ReactiveQueryByExampleExecutor<UsuarioData> {

    Mono<Boolean> existsByCorreoElectronico(String correoElectronico);
    Mono<Boolean> existsByTipoIdentificacionAndNumeroIdentificacion(String tipoIdentificacion, Long numeroIdentificacion);
}
