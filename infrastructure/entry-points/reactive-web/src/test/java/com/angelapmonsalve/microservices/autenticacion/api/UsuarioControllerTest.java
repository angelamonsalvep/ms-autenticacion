package com.angelapmonsalve.microservices.autenticacion.api;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {

    @Mock
    private RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

        when(registrarUsuarioUseCase.registrarUsuario(any()))
                .thenReturn(Mono.just(usuario));

        // Llamamos al método del controller directamente
        Mono<Usuario> response = usuarioController.registrarUsuario(usuario);

        // Verificamos el resultado con StepVerifier
        StepVerifier.create(response)
                .expectNextMatches(u ->
                        u.getId().equals("123") &&
                                u.getNombres().equals("Gael") &&
                                u.getApellidos().equals("Castillo")
                )
                .verifyComplete();
    }
}
