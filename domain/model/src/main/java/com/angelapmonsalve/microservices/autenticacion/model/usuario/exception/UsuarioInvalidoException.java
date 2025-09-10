package com.angelapmonsalve.microservices.autenticacion.model.usuario.exception;

public class UsuarioInvalidoException extends RuntimeException {
    public UsuarioInvalidoException(String mensaje) {
        super(mensaje);
    }
}
