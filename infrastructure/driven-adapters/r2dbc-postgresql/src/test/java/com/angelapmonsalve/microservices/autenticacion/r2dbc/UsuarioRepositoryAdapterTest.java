package com.angelapmonsalve.microservices.autenticacion.r2dbc;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.adapter.UsuarioRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @InjectMocks
    private UsuarioRepositoryAdapter repositoryAdapter;

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ObjectMapper mapper;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .id("1")
                .nombres("Angela")
                .correoElectronico("angela@test.com")
                .build();
    }

    @Test
    void mustGuardarUsuario() {
        Usuario usuario = buildUsuario();

        when(repository.guardar(usuario)).thenReturn(Mono.just(usuario));
        when(mapper.map(usuario, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.guardar(usuario);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getCorreoElectronico().equals("angela@test.com"))
                .verifyComplete();
    }

    @Test
    void mustValidarExistenciaPorCorreo() {
        when(repository.existePorCorreo("angela@test.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existePorCorreo("angela@test.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustRetornarFalseCuandoCorreoNoExiste() {
        when(repository.existePorCorreo("noexiste@test.com")).thenReturn(Mono.just(false));

        Mono<Boolean> result = repositoryAdapter.existePorCorreo("noexiste@test.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
