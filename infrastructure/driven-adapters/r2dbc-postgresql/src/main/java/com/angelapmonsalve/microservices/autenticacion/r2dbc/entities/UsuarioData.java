package com.angelapmonsalve.microservices.autenticacion.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("usuarios")
public class UsuarioData {

    @Id
    private UUID id;   // corresponde al tipo uuid en Postgres

    private String nombres;
    private String apellidos;

    @Column("fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String direccion;
    private String telefono;

    @Column("correo_electronico")
    private String correoElectronico;

    @Column("salario_base")
    private Double salarioBase;

    @Column("tipo_identificacion")
    private String tipoIdentificacion;

    @Column("numero_identificacion")
    private Long numeroIdentificacion;

}
