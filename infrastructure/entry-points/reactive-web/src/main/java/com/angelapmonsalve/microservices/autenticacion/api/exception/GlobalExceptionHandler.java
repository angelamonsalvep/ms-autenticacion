package com.angelapmonsalve.microservices.autenticacion.api.exception;

import com.angelapmonsalve.microservices.autenticacion.api.dto.ErrorResponseDTO;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.exception.UsuarioInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        var errorResponse = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(UsuarioInvalidoException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsuarioInvalidoException(UsuarioInvalidoException ex) {
        var errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(WebExchangeBindException ex) {
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        var errorResponse = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.name(),
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
