package com.angelapmonsalve.microservices.autenticacion.api.mapper;

import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioRequestDTO;
import com.angelapmonsalve.microservices.autenticacion.api.dto.UsuarioResponseDTO;
import com.angelapmonsalve.microservices.autenticacion.model.usuario.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static Usuario toDomain(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombres(dto.nombres())
                .apellidos(dto.apellidos())
                .fechaNacimiento(dto.fechaNacimiento())
                .direccion(dto.direccion())
                .telefono(dto.telefono())
                .correoElectronico(dto.correoElectronico())
                .salarioBase(dto.salarioBase())
                .build();
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getFechaNacimiento(),
                usuario.getCorreoElectronico(),
                usuario.getTelefono(),
                usuario.getDireccion(),
                usuario.getSalarioBase()
        );
    }
}
