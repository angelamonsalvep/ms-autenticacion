package com.angelapmonsalve.microservices.autenticacion.api.router;

import com.angelapmonsalve.microservices.autenticacion.api.handler.UsuarioHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UsuarioRouter {
    @Bean
    public RouterFunction<ServerResponse> usuarioRoutes(UsuarioHandler handler) {
        return route()
                .POST("/api/v1/usuarios", handler::registrarUsuario)
                .GET("/api/v1/usuarios/existe", handler::consultarExistenciaUsuario)
                .build();
    }
}
