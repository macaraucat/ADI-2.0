-- Roles
INSERT INTO bd_login.rol (codigo, nombre) VALUES 
('PASAJERO', 'Pasajero'),
('FUNC_ADUANA', 'Agente de Aduana'),
('FUNC_PDI', 'Agente PDI'),
('FUNC_SAG', 'Agente SAG'),
('ADMIN', 'Administrador');

-- Usuarios de prueba
INSERT INTO bd_login.usuario (tipo_documento, nro_documento, nombre_completo, email, password, organismo) VALUES
('RUT', '8888888-1', 'Juan Pérez García', 'juan.perez@example.com', 'password123', 'PASAJERO'),
('RUT', '8888888-2', 'María González López', 'maria.gonzalez@aduana.cl', 'aduana2024', 'ADUANA'),
('RUT', '8888888-3', 'Carlos Martínez Rodríguez', 'carlos.martinez@pdi.cl', 'pdi2024', 'PDI'),
('RUT', '8888888-4', 'Laura Rodríguez Fernández', 'laura.rodriguez@sag.cl', 'sag2024', 'SAG'),
('RUT', '9999999-9', 'Admin Sistema', 'admin@sistema.cl', 'admin123456', 'SISTEMA');

-- Asignar roles
INSERT INTO bd_login.usuario_rol VALUES
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-1'), (SELECT id FROM bd_login.rol WHERE codigo='PASAJERO')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-2'), (SELECT id FROM bd_login.rol WHERE codigo='FUNC_ADUANA')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-3'), (SELECT id FROM bd_login.rol WHERE codigo='FUNC_PDI')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-4'), (SELECT id FROM bd_login.rol WHERE codigo='FUNC_SAG')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='9999999-9'), (SELECT id FROM bd_login.rol WHERE codigo='ADMIN'));