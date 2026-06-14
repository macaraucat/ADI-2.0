CREATE SCHEMA IF NOT EXISTS bd_notificaciones;

CREATE TABLE bd_notificaciones.notificacion (
    id_notificacion BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    usuario_id BIGINT NOT NULL,
    tipo_notificacion VARCHAR(30) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_notificacion PRIMARY KEY (id_notificacion)
);