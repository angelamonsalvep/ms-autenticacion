package com.angelapmonsalve.microservices.autenticacion.usecase.autenticacion;

import com.angelapmonsalve.microservices.autenticacion.model.login.Login;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.LoginRepository;
import com.angelapmonsalve.microservices.autenticacion.model.login.gateways.TokenProvider;
import com.angelapmonsalve.microservices.autenticacion.model.rol.Rol;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.UsuarioNoEncontradoException;
import com.angelapmonsalve.microservices.autenticacion.model.login.exception.RolNoAutorizadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AutenticacionUseCaseTest {
    private LoginRepository loginRepository;
    private TokenProvider tokenProvider;
    private AutenticacionUseCase useCase;

    @BeforeEach
    void setUp() {
        loginRepository = Mockito.mock(LoginRepository.class);
        tokenProvider = Mockito.mock(TokenProvider.class);
        useCase = new AutenticacionUseCase(loginRepository, tokenProvider);
    }

    @Test
    void autenticar_exitoso() {
        Login input = Login.builder().correo("test@demo.com").clave("1234").rol(Rol.CLIENTE).build();
        Mockito.when(loginRepository.findByCorreo("test@demo.com")).thenReturn(Mono.just(input));
        Mockito.when(tokenProvider.generarToken(input)).thenReturn(Mono.just("token-jwt"));

        StepVerifier.create(useCase.autenticar(input))
                .expectNext("token-jwt")
                .verifyComplete();
    }

    @Test
    void autenticar_usuario_no_encontrado() {
        Login input = Login.builder().correo("no@demo.com").clave("1234").rol(Rol.CLIENTE).build();
        Mockito.when(loginRepository.findByCorreo("no@demo.com")).thenReturn(Mono.empty());

        StepVerifier.create(useCase.autenticar(input))
                .expectError(UsuarioNoEncontradoException.class)
                .verify();
    }

    @Test
    void autenticar_clave_incorrecta() {
        Login input = Login.builder().correo("test@demo.com").clave("wrong").rol(Rol.CLIENTE).build();
        Login db = Login.builder().correo("test@demo.com").clave("1234").rol(Rol.CLIENTE).build();
        Mockito.when(loginRepository.findByCorreo("test@demo.com")).thenReturn(Mono.just(db));

        StepVerifier.create(useCase.autenticar(input))
                .expectError(UsuarioNoEncontradoException.class)
                .verify();
    }

    @Test
    void autenticar_rol_no_autorizado() {
        Login input = Login.builder().correo("test@demo.com").clave("1234").rol(Rol.ADMIN).build();
        Login db = Login.builder().correo("test@demo.com").clave("1234").rol(Rol.CLIENTE).build();
        Mockito.when(loginRepository.findByCorreo("test@demo.com")).thenReturn(Mono.just(db));

        StepVerifier.create(useCase.autenticar(input))
                .expectError(RolNoAutorizadoException.class)
                .verify();
    }
}
