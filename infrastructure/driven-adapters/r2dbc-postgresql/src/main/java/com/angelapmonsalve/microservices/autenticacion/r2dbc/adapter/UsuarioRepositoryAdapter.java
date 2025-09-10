package com.angelapmonsalve.microservices.autenticacion.r2dbc.adapter;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.entities.UsuarioData;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.helper.ReactiveAdapterOperations;
import com.angelapmonsalve.microservices.autenticacion.r2dbc.repository.UsuarioDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UsuarioRepositoryAdapter extends ReactiveAdapterOperations<
        Usuario,          // ✅ Dominio
        UsuarioData,      // ✅ Entidad de infraestructura (tabla)
        String,           // ✅ Tipo de la primary key
        UsuarioDataRepository // ✅ CrudRepository reactivo
        > implements UsuarioRepository { // ✅ Implementa el puerto del dominio
    private final UsuarioDataRepository repository;
    private final ObjectMapper mapper;

    public UsuarioRepositoryAdapter(UsuarioDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Usuario.class));
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Mono<Usuario> guardar(Usuario usuario) {
        return repository.save(mapper.map(usuario, UsuarioData.class))
                .doOnSuccess(saved -> log.info("Usuario guardado con correo {}", saved.getCorreoElectronico()))
                .map(saved -> mapper.map(saved, Usuario.class));
    }

    @Override
    public Mono<Boolean> existePorCorreo(String correo) {
        return repository.existsByCorreoElectronico(correo);
    }

    @Override
    public Mono<Boolean> existePorTipoYNumeroIdentificacion(String tipoIdentificacion, Long numeroIdentificacion) {
        return repository.existsByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion);
    }
}
