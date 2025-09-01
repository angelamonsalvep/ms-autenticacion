package com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.exception.UsuarioInvalidoException;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegistrarUsuarioUseCaseTest {
    private static final String CORREO_ANA = "ana@mail.com";
    private static final String CORREO_LUIS = "luis@mail.com";
    private static final String CORREO_INVALIDO = "correo_invalido";
    private static final double SALARIO_VALIDO = 5000000.0;
    private static final double SALARIO_INVALIDO = -100.0;

    private UsuarioRepository usuarioRepository;
    private LoggerService loggerService;
    private RegistrarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        loggerService = mock(LoggerService.class);
        useCase = new RegistrarUsuarioUseCase(usuarioRepository, loggerService);
    }

    @Test
    void debeRegistrarUsuarioExitosamente() {
        Usuario usuario = Usuario.builder()
                .id("1")
                .nombres("Ana")
                .apellidos("Pérez")
                .correoElectronico(CORREO_ANA)
                .salarioBase(SALARIO_VALIDO)
                .build();
        when(usuarioRepository.existePorCorreo(CORREO_ANA)).thenReturn(Mono.just(false));
        when(usuarioRepository.guardar(any(Usuario.class))).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.registrarUsuario(usuario))
                .expectNext(usuario)
                .verifyComplete();

        verify(usuarioRepository).existePorCorreo(CORREO_ANA);
        verify(usuarioRepository).guardar(usuario);
        verify(loggerService, atLeastOnce()).info(anyString(), any());
    }

    @Test
    void debeRetornarErrorSiCorreoYaRegistrado() {
        Usuario usuario = Usuario.builder()
                .id("2")
                .nombres("Luis")
                .apellidos("Gómez")
                .correoElectronico(CORREO_LUIS)
                .salarioBase(8000000.0)
                .build();
        when(usuarioRepository.existePorCorreo(CORREO_LUIS)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.registrarUsuario(usuario))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().contains("El correo ya está registrado"))
                .verify();

        verify(usuarioRepository).existePorCorreo(CORREO_LUIS);
        verify(loggerService, atLeastOnce()).info(anyString(), any());
    }

    @Test
    void debeRetornarErrorSiDatosInvalidos() {
        // Usar una implementación dummy de LoggerService para evitar ambigüedad y NPE
        loggerService = new LoggerService() {
            @Override public void trace(String message, Object... args) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
            @Override public void debug(String message, Object... args) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
            @Override public void info(String message, Object... args) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
            @Override public void warn(String message, Object... args) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
            @Override public void error(String message, Object... args) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
            @Override public void error(String message, Throwable throwable) {
                // Método vacío solo para pruebas unitarias. No se requiere implementación.
                // SonarQube: Este método es un stub para evitar ambigüedad y NPE en los tests.
            }
        };
        usuarioRepository = new UsuarioRepository() {
            @Override public Mono<Boolean> existePorCorreo(String correo) {
                return Mono.just(false); // Valor seguro para evitar NPE
            }
            @Override public Mono<Usuario> guardar(Usuario usuario) {
                return Mono.empty(); // Valor seguro para evitar NPE
            }
        };
        useCase = new RegistrarUsuarioUseCase(usuarioRepository, loggerService);
        Usuario usuario = Usuario.builder()
                .id("3")
                .nombres("")
                .apellidos("Gómez")
                .correoElectronico(CORREO_INVALIDO)
                .salarioBase(SALARIO_INVALIDO)
                .build();
        StepVerifier.create(useCase.registrarUsuario(usuario))
                .expectError(UsuarioInvalidoException.class)
                .verify();
    }
}
