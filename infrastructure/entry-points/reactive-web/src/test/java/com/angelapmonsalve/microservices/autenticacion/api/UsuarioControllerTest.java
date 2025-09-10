package com.angelapmonsalve.microservices.autenticacion.api;

import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioResponseDTO;
import com.angelapmonsalve.microservices.autenticacion.api.mapper.UsuarioMapper;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = UsuarioController.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UsuarioController.class, UsuarioMapper.class, UsuarioControllerTest.TestConfig.class})
class UsuarioControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RegistrarUsuarioUseCase registrarUsuarioUseCase() {
            return org.mockito.Mockito.mock(RegistrarUsuarioUseCase.class);
        }
        @Bean
        public com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario.ConsultarExistenciaUsuarioUseCase consultarExistenciaUsuarioUseCase() {
            return org.mockito.Mockito.mock(com.angelapmonsalve.microservices.autenticacion.usecase.consultarexistenciausuario.ConsultarExistenciaUsuarioUseCase.class);
        }
    }

    @Test
    void deberiaRegistrarUsuario() {
        var usuario = Usuario.builder()
                .id("123")
                .nombres("Gael")
                .apellidos("Castillo")
                .fechaNacimiento(LocalDate.of(1995, 5, 15))
                .correoElectronico("gael.castillo@mail.com")
                .telefono("3001234567")
                .direccion("Calle Falsa 123")
                .salarioBase(3500.0)
                .build();

        when(registrarUsuarioUseCase.registrarUsuario(any(Usuario.class)))
                .thenReturn(Mono.just(usuario));

        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                3500.0,
                "CC",
                12345678901L
        );

        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UsuarioResponseDTO.class)
                .value(responseDto -> {
                    assert responseDto.id().equals("123");
                    assert responseDto.nombres().equals("Gael");
                });
    }

    @Test
    void deberiaRetornarErrorPorNombresVacios() {
        var request = new UsuarioRequestDTO(
                "",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorApellidosVacios() {
        var request = new UsuarioRequestDTO(
                "Gael",
                "",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorCorreoVacio() {
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorSalarioFueraDeRango() {
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                -1000.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorSalarioSuperiorAlLimite() {
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                20000000.0 // mayor al límite
                , "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorCorreoInvalido() {
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "correo-invalido",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void deberiaRetornarErrorPorCorreoDuplicado() {
        when(registrarUsuarioUseCase.registrarUsuario(any()))
                .thenReturn(Mono.error(new RuntimeException("El correo ya está registrado.")));
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void deberiaRetornarErrorPorExcepcionInesperada() {
        when(registrarUsuarioUseCase.registrarUsuario(any()))
                .thenReturn(Mono.error(new RuntimeException("Error inesperado")));
        var request = new UsuarioRequestDTO(
                "Gael",
                "Castillo",
                LocalDate.of(1995, 5, 15),
                "Calle Falsa 123",
                "3001234567",
                "gael.castillo@mail.com",
                3500.0,
                "CC",
                12345678901L
        );
        webTestClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError();
    }

}
