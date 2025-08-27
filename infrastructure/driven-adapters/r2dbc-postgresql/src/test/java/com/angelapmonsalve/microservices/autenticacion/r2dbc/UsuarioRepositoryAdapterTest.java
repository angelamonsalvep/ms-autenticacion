package com.angelapmonsalve.microservices.autenticacion.r2dbc;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.adapter.UsuarioRepositoryAdapter;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.entities.UsuarioData;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.repository.UsuarioDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioDataRepository usuarioDataRepository;

    @Mock
    private ObjectMapper mapper;

    private UsuarioRepositoryAdapter repositoryAdapter;

    private Usuario buildUsuario() {
        return Usuario.builder()
                .id("1")
                .nombres("Angela")
                .correoElectronico("angela@test.com")
                .build();
    }

    private UsuarioData buildUsuarioData() {
        UsuarioData data = new UsuarioData();
        data.setId("1");
        data.setNombres("Angela");
        data.setCorreoElectronico("angela@test.com");
        return data;
    }

    @BeforeEach
    void setUp() {
        repositoryAdapter = new UsuarioRepositoryAdapter(usuarioDataRepository, mapper);
    }

    @Test
    void mustGuardarUsuario() {
        Usuario usuario = buildUsuario();
        UsuarioData usuarioData = buildUsuarioData();

        when(mapper.map(usuario, UsuarioData.class)).thenReturn(usuarioData);
        when(usuarioDataRepository.save(usuarioData)).thenReturn(Mono.just(usuarioData));
        when(mapper.map(usuarioData, Usuario.class)).thenReturn(usuario);

        Mono<Usuario> result = repositoryAdapter.guardar(usuario);

        StepVerifier.create(result)
                .expectNextMatches(u -> u.getCorreoElectronico().equals("angela@test.com"))
                .verifyComplete();
    }

    @Test
    void mustValidarExistenciaPorCorreo() {
        when(usuarioDataRepository.existsByCorreoElectronico("angela@test.com")).thenReturn(Mono.just(true));

        Mono<Boolean> result = repositoryAdapter.existePorCorreo("angela@test.com");

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustRetornarFalseCuandoCorreoNoExiste() {
        when(usuarioDataRepository.existsByCorreoElectronico("noexiste@test.com")).thenReturn(Mono.just(false));

        Mono<Boolean> result = repositoryAdapter.existePorCorreo("noexiste@test.com");

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }
}
