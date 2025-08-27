package com.angelapmonsalve.microservices.autenticacion.config;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public RegistrarUsuarioUseCase registrarUsuarioUseCase(
            UsuarioRepository usuarioRepository,
            LoggerService loggerService
    ) {
        return new RegistrarUsuarioUseCase(usuarioRepository, loggerService);
    }

    // 👉 Aquí solo defines casos de uso, nunca adapters.
}
