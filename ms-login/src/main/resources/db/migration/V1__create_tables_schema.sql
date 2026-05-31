CREATE SCHEMA IF NOT EXISTS bd_login;

CREATE TABLE bd_login.rol (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE bd_login.permiso (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo VARCHAR(30) UNIQUE NOT NULL,
    descripcion VARCHAR(100)
);

CREATE TABLE bd_login.rol_permiso (
    id_rol BIGINT NOT NULL,
    id_permiso BIGINT NOT NULL,
    PRIMARY KEY (id_rol, id_permiso),
    FOREIGN KEY (id_rol) REFERENCES bd_login.rol(id) ON DELETE CASCADE,
    FOREIGN KEY (id_permiso) REFERENCES bd_login.permiso(id) ON DELETE CASCADE
);

CREATE TABLE bd_login.usuario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    tipo_documento VARCHAR(10) NOT NULL,
    nro_documento VARCHAR(10) UNIQUE NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(12) NOT NULL,
    organismo VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    ultimo_acceso TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE bd_login.usuario_rol (
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_rol),
    FOREIGN KEY (id_usuario) REFERENCES bd_login.usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES bd_login.rol(id) ON DELETE CASCADE
);