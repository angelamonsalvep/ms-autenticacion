package com.angelapmonsalve.microservices.autenticacion.model.login.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super("Usuario o clave incorrectos");
    }
}

