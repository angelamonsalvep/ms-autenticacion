package com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import reactor.core.publisher.Mono;

public interface UsuarioRepository {
    Mono<Usuario> guardar(Usuario usuario);
    Mono<Boolean> existePorCorreo(String correo);
}
