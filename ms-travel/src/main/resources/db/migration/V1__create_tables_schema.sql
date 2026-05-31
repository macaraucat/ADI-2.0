CREATE SCHEMA IF NOT EXISTS db_travel_service;

--Tabla Estado
CREATE TABLE db_travel_service.estado (
    id_estado INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    nombre_estado VARCHAR(20) NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    
    CONSTRAINT pk_estado PRIMARY KEY (id_estado) 
);

--Tabla Tramite
CREATE TABLE db_travel_service.tramite (
    id_tramite BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    usuario_id BIGINT NOT NULL,
    patente_tranposte VARCHAR(10) NOT NULL,
    tipo_tramite VARCHAR(10) NOT NULL,
    fecha_de_ingreso TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado_global VARCHAR(20) NOT NULL,

    CONSTRAINT pk_tramite PRIMARY KEY (id_tramite)
);

--Tabla Pasajero
CREATE TABLE db_travel_service.pasajero (
    id_pasajero BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    tipo_documento VARCHAR(15) NOT NULL,
    nro_documento VARCHAR(15) NOT NULL,
    nacionalidad VARCHAR(15) NOT NULL,
    fecha_nacimiento TIMESTAMP NOT NULL,
    id_tramite BIGINT NOT NULL,

    CONSTRAINT pk_pasajero PRIMARY KEY (id_pasajero),
    CONSTRAINT fk_pasajero_tramite FOREIGN KEY (id_tramite) 
        REFERENCES db_travel_service.tramite(id_tramite)
);

-- Tabla Tramite_Estado
CREATE TABLE db_travel_service.tramite_estado (
    tramite_estado_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    id_tramite BIGINT NOT NULL,
    id_estado INT NOT NULL,
    fecha_cambio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    comentario VARCHAR(200),

    CONSTRAINT pk_tramite_estado PRIMARY KEY (tramite_estado_id),
    CONSTRAINT fk_te_tramite FOREIGN KEY (id_tramite) 
        REFERENCES db_travel_service.tramite(id_tramite),
    CONSTRAINT fk_te_estado FOREIGN KEY (id_estado) 
        REFERENCES db_travel_service.estado(id_estado)
);