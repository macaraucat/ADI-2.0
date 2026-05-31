CREATE SCHEMA IF NOT EXISTS bd_reportes;

CREATE TABLE bd_reportes.reporte (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(10) NOT NULL,
    fecha_generacion TIMESTAMP NOT NULL,
    total_registros INT DEFAULT 0,
    generado_por VARCHAR(255) NOT NULL,
    formato VARCHAR(50) DEFAULT 'JSON'
);

-- REPORTES DE PRUEBA
INSERT INTO bd_reportes.reporte (tipo, fecha_generacion, total_registros, generado_por, formato) VALUES
('USUARIOS', CURRENT_TIMESTAMP, 5, 'admin@sistema.cl', 'JSON'),
('TRAMITES', CURRENT_TIMESTAMP, 0, 'maria.gonzalez@aduana.cl', 'JSON'),
('VEHICULOS', CURRENT_TIMESTAMP, 0, 'admin@sistema.cl', 'JSON');