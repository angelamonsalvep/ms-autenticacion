package com.angelapmonsalve.microservices.autenticacion.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Autenticación API")
                        .version("1.0.0")
                        .description("Documentación de la API de autenticación")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("backend@empresa.com")
                        )
                );
    }

    @Bean
    public GroupedOpenApi usuariosApi() {
        return GroupedOpenApi.builder()
                .group("usuarios")
                .pathsToMatch("/api/v1/usuarios/**")
                .build();
    }
}
