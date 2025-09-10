package com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways;

public interface LoggerService {
    void trace(String message, Object... args);
    void debug(String message, Object... args);
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Object... args);
    void error(String message, Throwable throwable);
}
