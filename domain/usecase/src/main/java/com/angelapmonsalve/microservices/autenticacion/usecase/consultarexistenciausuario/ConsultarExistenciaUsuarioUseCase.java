package com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ConsultarExistenciaUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public Mono<Boolean> existePorTipoYNumeroIdentificacion(String tipoIdentificacion, Long numeroIdentificacion) {
        return usuarioRepository.existePorTipoYNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion);
    }
}

