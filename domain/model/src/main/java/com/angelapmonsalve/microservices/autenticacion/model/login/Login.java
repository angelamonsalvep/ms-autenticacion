package com.angelapmonsalve.microservices.autenticacion.model.login;

import com.angelapmonsalve.microservices.autenticacion.model.rol.Rol;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Login {
    private String correo;
    private String clave;
    private Rol rol;
}
