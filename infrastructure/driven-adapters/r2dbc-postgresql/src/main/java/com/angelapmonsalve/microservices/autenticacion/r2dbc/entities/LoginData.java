package com.angelapmonsalve.microservices.autenticacion.r2dbc.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import com.angelapmonsalve.microservices.autenticacion.model.rol.Rol;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("login")
public class LoginData {
    @Id
    private String correo;
    private String clave;
    private Rol rol;
}

