package com.angelapmonsalve.microservices.autenticacion.api.router;

import com.angelapmonsalve.microservices.autenticacion.api.handler.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(LoginHandler handler) {
        return route(POST("/api/v1/login"), handler::listenPOSTLoginUseCase);
    }
}
