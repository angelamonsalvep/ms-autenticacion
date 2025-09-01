package com.angelapmonsalve.microservices.autenticacion.model.usuario;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    @Test
    void crearUsuarioConConstructor() {
        Usuario usuario = new Usuario("1", "Ana", "Pérez", LocalDate.of(1990, 1, 1), "Calle 123", "1234567890", "ana@mail.com", 5000000.0);
        assertEquals("Ana", usuario.getNombres());
        assertEquals("Pérez", usuario.getApellidos());
        assertEquals("ana@mail.com", usuario.getCorreoElectronico());
        assertEquals(5000000.0, usuario.getSalarioBase());
    }

    @Test
    void crearUsuarioConBuilder() {
        Usuario usuario = Usuario.builder()
                .id("2")
                .nombres("Luis")
                .apellidos("Gómez")
                .fechaNacimiento(LocalDate.of(1985, 5, 20))
                .direccion("Avenida 456")
                .telefono("0987654321")
                .correoElectronico("luis@mail.com")
                .salarioBase(8000000.0)
                .build();
        assertEquals("Luis", usuario.getNombres());
        assertEquals("Gómez", usuario.getApellidos());
        assertEquals("luis@mail.com", usuario.getCorreoElectronico());
        assertEquals(8000000.0, usuario.getSalarioBase());
    }

    @Test
    void settersYGettersFuncionan() {
        Usuario usuario = new Usuario();
        usuario.setNombres("Carlos");
        usuario.setApellidos("Ramírez");
        usuario.setCorreoElectronico("carlos@mail.com");
        usuario.setSalarioBase(1000000.0);
        assertEquals("Carlos", usuario.getNombres());
        assertEquals("Ramírez", usuario.getApellidos());
        assertEquals("carlos@mail.com", usuario.getCorreoElectronico());
        assertEquals(1000000.0, usuario.getSalarioBase());
    }
}

