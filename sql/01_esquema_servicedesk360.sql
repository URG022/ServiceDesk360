CREATE DATABASE IF NOT EXISTS servicedesk360_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;

USE servicedesk360_db;

CREATE TABLE clientes (
    id_cliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE equipos (
    id_equipo BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    codigo_inventario VARCHAR(40) NOT NULL UNIQUE,
    tipo VARCHAR(80) NOT NULL,
    marca VARCHAR(80),
    modelo VARCHAR(80),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_equipo_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

CREATE TABLE tecnicos (
    id_tecnico BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    especialidad VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE categorias (
    id_categoria BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE tickets (
    id_ticket BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    id_equipo BIGINT NULL,
    id_tecnico BIGINT NULL,
    id_categoria BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    prioridad VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ABIERTO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ticket_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    CONSTRAINT fk_ticket_equipo FOREIGN KEY (id_equipo) REFERENCES equipos(id_equipo),
    CONSTRAINT fk_ticket_tecnico FOREIGN KEY (id_tecnico) REFERENCES tecnicos(id_tecnico),
    CONSTRAINT fk_ticket_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    CONSTRAINT chk_ticket_prioridad CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA')),
    CONSTRAINT chk_ticket_estado CHECK (estado IN ('ABIERTO', 'ASIGNADO', 'EN_PROCESO', 'CERRADO'))
);

CREATE TABLE seguimientos (
    id_seguimiento BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_ticket BIGINT NOT NULL,
    detalle VARCHAR(800) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_seguimiento_ticket FOREIGN KEY (id_ticket) REFERENCES tickets(id_ticket)
);