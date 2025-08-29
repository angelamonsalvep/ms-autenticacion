package com.angelapmonsalve.microservices.autenticacion.api.config;

import com.angelapmonsalve.microservices.autenticacion.api.UsuarioController;
import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest
@ContextConfiguration(classes = {UsuarioController.class, ConfigTest.MockConfig.class})
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @TestConfiguration
    static class MockConfig {
        @Bean
        RegistrarUsuarioUseCase registrarUsuarioUseCase() {
            return Mockito.mock(RegistrarUsuarioUseCase.class);
        }
    }

    @Test
    void corsConfigurationShouldAllowOrigins() {
        // dado un usuario mock
        Usuario mockUsuario = Usuario.builder()
                .id("123")
                .nombres("Test")
                .apellidos("User")
                .fechaNacimiento(LocalDate.now().minusYears(20))
                .correoElectronico("test@user.com")
                .build();
        Mockito.when(registrarUsuarioUseCase.registrarUsuario(any()))
                .thenReturn(Mono.just(mockUsuario));

        UsuarioRequestDTO requestDTO = new UsuarioRequestDTO(
                "Test",
                "User",
                LocalDate.now().minusYears(20),
                "address",
                "1234567",
                "test@user.com",
                50000.0
        );

        // cuando hago POST al controlador real
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .bodyValue(requestDTO)
                .exchange()
                // entonces espero status CREATED y headers de seguridad
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}
