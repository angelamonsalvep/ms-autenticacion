package com.angelapmonsalve.microservices.autenticacion.usecase.autenticacion;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.LoginRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AutenticacionUseCase {
    private final LoginRepository loginRepository;

    public Mono<String> autenticar(Login login) {
        return loginRepository.findByCorreo(login.getCorreo())
            .flatMap(usuario -> {
                if (usuario == null || !usuario.getClave().equals(login.getClave())) {
                    return Mono.error(new RuntimeException("Usuario o clave incorrectos"));
                }
                // Validar el rol si es necesario
                if (login.getRol() != null && usuario.getRol() != login.getRol()) {
                    return Mono.error(new RuntimeException("Rol no autorizado"));
                }
                return loginRepository.generarToken(usuario);
            });
    }
}
