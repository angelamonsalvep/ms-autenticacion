package com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.exception.UsuarioInvalidoException;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegistrarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final LoggerService logger;

    public Mono<Usuario> registrarUsuario(Usuario usuario) {
        logger.info("Iniciando registro de usuario con correo {}", usuario.getCorreoElectronico());

        return validarUsuario(usuario)
                .then(usuarioRepository.existePorTipoYNumeroIdentificacion(usuario.getTipoIdentificacion(), usuario.getNumeroIdentificacion())
                        .flatMap(existeDoc -> {
                            if (Boolean.TRUE.equals(existeDoc)) {
                                return Mono.error(new UsuarioInvalidoException("Ya existe un usuario con ese tipo y número de identificación"));
                            }
                            return usuarioRepository.existePorCorreo(usuario.getCorreoElectronico())
                                    .flatMap(existeCorreo -> {
                                        if (Boolean.TRUE.equals(existeCorreo)) {
                                            return Mono.error(new UsuarioInvalidoException("El correo ya está registrado"));
                                        }
                                        return usuarioRepository.guardar(usuario);
                                    });
                        })
                )
                .doOnSuccess(u -> logger.info("Usuario registrado con éxito: {}", u.getId()))
                .doOnError(e -> logger.error("Error registrando usuario: {}", e.getMessage()));
    }

    private Mono<Void> validarUsuario(Usuario usuario) {
        if (Objects.isNull(usuario.getNombres()) || usuario.getNombres().isBlank()) {
            return Mono.error(new UsuarioInvalidoException("El nombre es obligatorio"));
        }
        if (Objects.isNull(usuario.getApellidos()) || usuario.getApellidos().isBlank()) {
            return Mono.error(new UsuarioInvalidoException("El apellido es obligatorio"));
        }
        if (Objects.isNull(usuario.getCorreoElectronico()) || !esCorreoValido(usuario.getCorreoElectronico())) {
            return Mono.error(new UsuarioInvalidoException("Correo electrónico inválido"));
        }
        if (Objects.isNull(usuario.getSalarioBase()) || usuario.getSalarioBase() <= 0 || usuario.getSalarioBase() > 15000000) {
            return Mono.error(new UsuarioInvalidoException("El salario base debe estar entre 0 y 15,000,000"));
        }
        return Mono.empty();
    }

    private boolean esCorreoValido(String correo) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matcher(correo).matches();
    }
}
