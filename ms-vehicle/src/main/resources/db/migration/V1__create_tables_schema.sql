CREATE SCHEMA IF NOT EXISTS bd_vehiculos;

CREATE TABLE bd_vehiculos.vehiculos (
    id_vehiculo BIGSERIAL PRIMARY KEY,
    patente VARCHAR(20),
    pais_registro VARCHAR(100),
    vin VARCHAR(50),
    marca VARCHAR(100),
    modelo VARCHAR(100)
);

CREATE TABLE bd_vehiculos.admisiones (
    id_admision_temporal BIGSERIAL PRIMARY KEY,
    fecha_salida DATE,
    fecha_vencimiento DATE,
    paso_salida VARCHAR(100),
    estado VARCHAR(50),
    id_vehiculo BIGINT REFERENCES bd_vehiculos.vehiculos(id_vehiculo)
);

CREATE TABLE bd_vehiculos.conductores_autorizados (
    id_conductor BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200),
    nro_documento VARCHAR(50),
    id_admision_temporal BIGINT REFERENCES bd_vehiculos.admisiones(id_admision_temporal)
);