-- Roles
INSERT INTO bd_login.rol (codigo, nombre) VALUES 
('PASAJERO', 'Pasajero'),
('AGENTE_ADUANA', 'Agente de Aduana'),
('AGENTE_PDI', 'Agente PDI'),
('AGENTE_SAG', 'Agente SAG'),
('ADMIN', 'Administrador');

-- Permisos
INSERT INTO bd_login.permiso (codigo, descripcion) VALUES 
('DECLARACION_VALIDAR', 'Permiso para validar declaraciones'),
('VEHICULO_APROBAR', 'Permiso para aprobar vehículos'),
('INSPECCION_REALIZAR', 'Permiso para realizar inspecciones'),
('REPORTE_VER', 'Permiso para ver reportes'),
('USUARIO_GESTIONAR', 'Permiso para gestionar usuarios'),
('ALERTA_CREAR', 'Permiso para crear alertas');

-- Asignación permisos (ahora con los códigos correctos)
INSERT INTO bd_login.rol_permiso (id_rol, id_permiso) VALUES
((SELECT id FROM bd_login.rol WHERE codigo = 'PASAJERO'), (SELECT id FROM bd_login.permiso WHERE codigo = 'REPORTE_VER'));

INSERT INTO bd_login.rol_permiso (id_rol, id_permiso) VALUES
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_ADUANA'), (SELECT id FROM bd_login.permiso WHERE codigo = 'DECLARACION_VALIDAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_ADUANA'), (SELECT id FROM bd_login.permiso WHERE codigo = 'VEHICULO_APROBAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_ADUANA'), (SELECT id FROM bd_login.permiso WHERE codigo = 'INSPECCION_REALIZAR'));

INSERT INTO bd_login.rol_permiso (id_rol, id_permiso) VALUES
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_PDI'), (SELECT id FROM bd_login.permiso WHERE codigo = 'INSPECCION_REALIZAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_PDI'), (SELECT id FROM bd_login.permiso WHERE codigo = 'ALERTA_CREAR'));

INSERT INTO bd_login.rol_permiso (id_rol, id_permiso) VALUES
((SELECT id FROM bd_login.rol WHERE codigo = 'AGENTE_SAG'), (SELECT id FROM bd_login.permiso WHERE codigo = 'INSPECCION_REALIZAR'));

INSERT INTO bd_login.rol_permiso (id_rol, id_permiso) VALUES
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'DECLARACION_VALIDAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'VEHICULO_APROBAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'INSPECCION_REALIZAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'REPORTE_VER')),
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'USUARIO_GESTIONAR')),
((SELECT id FROM bd_login.rol WHERE codigo = 'ADMIN'), (SELECT id FROM bd_login.permiso WHERE codigo = 'ALERTA_CREAR'));

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
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-2'), (SELECT id FROM bd_login.rol WHERE codigo='AGENTE_ADUANA')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-3'), (SELECT id FROM bd_login.rol WHERE codigo='AGENTE_PDI')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='8888888-4'), (SELECT id FROM bd_login.rol WHERE codigo='AGENTE_SAG')),
((SELECT id FROM bd_login.usuario WHERE nro_documento='9999999-9'), (SELECT id FROM bd_login.rol WHERE codigo='ADMIN'));