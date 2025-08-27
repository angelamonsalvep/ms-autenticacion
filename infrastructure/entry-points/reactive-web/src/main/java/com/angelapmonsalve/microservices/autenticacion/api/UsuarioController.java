package com.angelapmonsalve.microservices.autenticacion.api;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;
import com.angelapmonsalve.microservices.autenticacion.usecase.registrarusuario.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @PostMapping
    public Mono<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        return registrarUsuarioUseCase.registrarUsuario(usuario);
    }
}
