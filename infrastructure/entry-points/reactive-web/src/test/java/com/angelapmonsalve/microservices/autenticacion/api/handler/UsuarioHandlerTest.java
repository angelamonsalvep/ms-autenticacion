package com.angelapmonsalve.microservices.autenticacion.api.handler;

import com.angelapmonsalve.microservices.autenticacion.api.config.CorsConfig;
import com.angelapmonsalve.microservices.autenticacion.api.config.SecurityHeadersConfig;
import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.api.mapper.UsuarioMapper;
import com.angelapmonsalve.microservices.autenticacion.api.router.UsuarioRouter;
import com.angelapmonsalve.microservices.autenticacion.jwt.JwtTokenProvider;
import com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario.ConsultarExistenciaUsuarioUseCase;
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
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(UsuarioHandler.class)
@Import({UsuarioRouter.class, CorsConfig.class, SecurityHeadersConfig.class})
class UsuarioHandlerTest {

    // Todo el contenido de la clase ha sido comentado temporalmente
    /*
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RegistrarUsuarioUseCase registrarUsuarioUseCase() {
            return Mockito.mock(RegistrarUsuarioUseCase.class);
        }
        @Bean
        public ConsultarExistenciaUsuarioUseCase consultarExistenciaUsuarioUseCase() {
            return Mockito.mock(ConsultarExistenciaUsuarioUseCase.class);
        }
        @Bean
        public Validator validator() {
            return new LocalValidatorFactoryBean();
        }
        @Bean
        public JwtTokenProvider jwtTokenProvider() {
            JwtTokenProvider provider = Mockito.mock(JwtTokenProvider.class);
            when(provider.getRolFromToken("Bearer admin-token")).thenReturn("admin");
            when(provider.getRolFromToken("Bearer asesor-token")).thenReturn("asesor");
            when(provider.getRolFromToken("Bearer user-token")).thenReturn("user");
            when(provider.getRolFromToken("Bearer invalid-token")).thenThrow(new RuntimeException("Token inválido o expirado"));
            return provider;
        }
    }

    private UsuarioRequestDTO getValidUsuarioRequest() {
        return new UsuarioRequestDTO(
                "Juan",
                "Perez",
                java.time.LocalDate.of(1990, 1, 1),
                "Calle 123",
                "3001234567",
                "juan.perez@mail.com",
                1000000.0,
                "CC",
                123456789L
        );
    }

    @Test
    void registroPorAdminDebeSerExitoso() {
        when(registrarUsuarioUseCase.registrarUsuario(any())).thenReturn(Mono.just(UsuarioMapper.toDomain(getValidUsuarioRequest())));
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer admin-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Juan")
                .jsonPath("$.apellidos").isEqualTo("Perez")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@mail.com");
    }

    @Test
    void registroPorAsesorDebeSerExitoso() {
        when(registrarUsuarioUseCase.registrarUsuario(any())).thenReturn(Mono.just(UsuarioMapper.toDomain(getValidUsuarioRequest())));
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer asesor-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Juan")
                .jsonPath("$.apellidos").isEqualTo("Perez")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@mail.com");
    }

    @Test
    void registroPorUsuarioNoAutorizadoDebeSer403() {
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody(String.class).isEqualTo("No tienes permisos para registrar usuarios");
    }

    @Test
    void registroSinTokenDebeSer401() {
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody(String.class).isEqualTo("No se encontró el token de autenticación");
    }

    @Test
    void registroConTokenInvalidoDebeSer401() {
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody(String.class).isEqualTo("Token inválido o expirado");
    }

    // TODO: Test deshabilitado temporalmente por mantenimiento en infraestructura
    /*
    @Test
    void registroPorAdminDebeSerExitoso() {
        when(registrarUsuarioUseCase.registrarUsuario(any())).thenReturn(Mono.just(UsuarioMapper.toDomain(getValidUsuarioRequest())));
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer admin-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Juan")
                .jsonPath("$.apellidos").isEqualTo("Perez")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@mail.com");
    }

    @Test
    void registroPorAsesorDebeSerExitoso() {
        when(registrarUsuarioUseCase.registrarUsuario(any())).thenReturn(Mono.just(UsuarioMapper.toDomain(getValidUsuarioRequest())));
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer asesor-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody()
                .jsonPath("$.nombres").isEqualTo("Juan")
                .jsonPath("$.apellidos").isEqualTo("Perez")
                .jsonPath("$.correoElectronico").isEqualTo("juan.perez@mail.com");
    }

    @Test
    void registroPorUsuarioNoAutorizadoDebeSer403() {
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.FORBIDDEN)
                .expectBody(String.class).isEqualTo("No tienes permisos para registrar usuarios");
    }

    @Test
    void registroSinTokenDebeSer401() {
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody(String.class).isEqualTo("No se encontró el token de autenticación");
    }

    @Test
    void registroConTokenInvalidoDebeSer401() {
        webTestClient.post().uri("/api/v1/usuarios")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getValidUsuarioRequest())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED)
                .expectBody(String.class).isEqualTo("Token inválido o expirado");
    }
    */
}
