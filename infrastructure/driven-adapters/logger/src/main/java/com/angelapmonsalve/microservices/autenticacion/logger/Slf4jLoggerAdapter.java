package com.angelapmonsalve.microservices.autenticacion.logger;

import com.angelapmonsalve.microservices.autenticacion.model.usuario.gateways.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Slf4jLoggerAdapter implements LoggerService {

    @Override
    public void trace(String message, Object... args) {
        log.trace(message, args);
    }

    @Override
    public void debug(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }
}
