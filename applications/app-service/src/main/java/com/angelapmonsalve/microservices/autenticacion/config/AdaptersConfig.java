package com.angelapmonsalve.microservices.autenticacion.config;

import com.angelapmonsalve.microservices.autenticacion.jwt.JwtTokenProvider;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.TokenProvider;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.adapter.UsuarioRepositoryAdapter;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.repository.UsuarioDataRepository;
import com.angelapmonsalve.microservices.autenticacion.logger.Slf4jLoggerAdapter;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdaptersConfig {

    @Bean
    public UsuarioRepository usuarioRepository(UsuarioDataRepository repository, ObjectMapper mapper) {
        return new UsuarioRepositoryAdapter(repository, mapper);
    }

    @Bean
    public LoggerService loggerService() {
        return new Slf4jLoggerAdapter();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider();
    }
}
