package com.angelapmonsalve.microservices.autenticacion.api.exception;

import com.angelapmonsalve.microservices.autenticacion.api.UsuarioController;
import com.angelapmonsalve.microservices.autenticacion.api.config.CorsConfig;
import com.angelapmonsalve.microservices.autenticacion.api.config.SecurityHeadersConfig;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {GlobalExceptionHandler.class, UsuarioController.class, GlobalExceptionHandlerTest.TestConfig.class})
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RegistrarUsuarioUseCase registrarUsuarioUseCase() {
            return Mockito.mock(RegistrarUsuarioUseCase.class);
        }
    }

    @Test
    void shouldHandleWebExchangeBindException() {
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("BAD_REQUEST")
                .jsonPath("$.mensaje").isNotEmpty();
    }

    @Test
    void shouldHandleGenericException() {
        when(registrarUsuarioUseCase.registrarUsuario(any()))
                .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombres\":\"Test\",\"apellidos\":\"User\",\"fechaNacimiento\":\"1990-01-01\",\"direccion\":\"address\",\"telefono\":\"1234567\",\"correoElectronico\":\"test@test.com\",\"salarioBase\":1000}")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.codigo").isEqualTo("INTERNAL_SERVER_ERROR")
                .jsonPath("$.mensaje").isEqualTo("Unexpected error");
    }
}
