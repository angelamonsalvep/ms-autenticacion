package com.angelapmonsalve.microservices.autenticacion.model.login.exception;

public class RolNoAutorizadoException extends RuntimeException {
    public RolNoAutorizadoException() {
        super("Rol no autorizado");
    }
}

