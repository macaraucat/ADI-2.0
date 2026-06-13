CREATE SCHEMA IF NOT EXISTS bd_tramites;

-- 1. Tabla Estado
CREATE TABLE bd_tramites.estado (
    id_estado INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    nombre_estado VARCHAR(20) NOT NULL,
    descripcion VARCHAR(200),
    
    CONSTRAINT pk_estado PRIMARY KEY (id_estado),
    CONSTRAINT uk_nombre_estado UNIQUE (nombre_estado)
);

-- 2. Tabla Tramite
CREATE TABLE bd_tramites.tramite (
    id_tramite BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    id_usuario BIGINT,
    id_vehiculo BIGINT,
    tipo_tramite VARCHAR(20),
    fecha_de_ingreso TIMESTAMP,
    estado_actual VARCHAR(20),

    CONSTRAINT pk_tramite PRIMARY KEY (id_tramite)
);

-- 3. Tabla Pasajero
CREATE TABLE bd_tramites.pasajero (
    id_pasajero BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    tipo_documento VARCHAR(15) NOT NULL,
    nro_documento VARCHAR(15) NOT NULL,
    nacionalidad VARCHAR(15) NOT NULL,
    fecha_nacimiento DATE NOT NULL, -- Corregido directamente a DATE según tu V2 anterior
    id_tramite BIGINT,

    CONSTRAINT pk_pasajero PRIMARY KEY (id_pasajero),
    CONSTRAINT fk_pasajero_tramite FOREIGN KEY (id_tramite) 
        REFERENCES bd_tramites.tramite(id_tramite) ON DELETE CASCADE
);

-- 4. Tabla Estado_Tramite (Corregido el nombre y orden para hacer match con la Entidad)
CREATE TABLE bd_tramites.estado_tramite (
    tramite_estado_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    id_tramite BIGINT NOT NULL,
    id_estado INT NOT NULL,
    fecha_cambio TIMESTAMP,
    comentario VARCHAR(200),

    CONSTRAINT pk_estado_tramite PRIMARY KEY (tramite_estado_id),
    CONSTRAINT fk_et_tramite FOREIGN KEY (id_tramite) 
        REFERENCES bd_tramites.tramite(id_tramite) ON DELETE CASCADE,
    CONSTRAINT fk_et_estado FOREIGN KEY (id_estado) 
        REFERENCES bd_tramites.estado(id_estado)
);