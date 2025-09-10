package com.angelapmonsalve.microservices.autenticacion.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private String codigo;
    private String mensaje;
}

