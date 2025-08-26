-- Crear esquema
create schema if not exists auth;

-- Habilitar extensión para UUID
create extension if not exists "pgcrypto";

-- Crear tabla
create table if not exists auth.usuarios (
  id uuid primary key default gen_random_uuid(),
  nombres varchar(120) not null,
  apellidos varchar(120) not null,
  fecha_nacimiento date,
  direccion varchar(200),
  telefono varchar(50),
  correo_electronico varchar(180) not null,
  salario_base numeric(15,2) not null,
  creado_en timestamptz not null default now()
);

-- Índice único
create unique index if not exists uk_usuarios_correo on auth.usuarios(correo_electronico);
