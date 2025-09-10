package com.angelapmonsalve.microservices.autenticacion.r2dbc.adapter;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.LoginRepository;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.entities.LoginData;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.repository.LoginDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LoginRepositoryAdapter implements LoginRepository {
    private final LoginDataRepository loginDataRepository;

    @Override
    public Mono<Login> findByCorreo(String correo) {
        return loginDataRepository.findByCorreo(correo)
                .map(data -> Login.builder()
                        .correo(data.getCorreo())
                        .clave(data.getClave())
                        .rol(data.getRol())
                        .build());
    }
}

