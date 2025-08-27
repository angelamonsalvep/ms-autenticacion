package com.angelapmonsalve.microservices.autenticacion.api.config;

import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {
    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioRepository usuarioRepository, LoggerService loggerService) {
        return new RegistrarUsuarioUseCase(usuarioRepository, loggerService);
    }
}
