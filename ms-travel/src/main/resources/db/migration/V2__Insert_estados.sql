INSERT INTO bd_tramites.estado (nombre_estado, descripcion) VALUES
('INGRESADO', 'El trámite ha sido creado e ingresado exitosamente al sistema.'),
('EN_REVISION', 'El trámite se encuentra bajo análisis técnico o de datos.'),
('APROBADO_AUTOMATICO', 'El trámite aprobó todas las validaciones de riesgo automatizadas.'),
('RECHAZADO_AUTOMATICO', 'El trámite fue denegado de forma inmediata por el motor de reglas.'),
('CANCELADO_POR_USUARIO', 'El trámite fue dado de baja o anulado por el propio solicitante.'),
('REQUIERE_INSPECCION', 'El análisis determinó que el vehículo o pasajero debe ir a revisión física en frontera.'),
('INSPECCION_EN_CURSO', 'El oficial de aduanas se encuentra inspeccionando físicamente la comitiva.'),
('RECHAZADO_EN_INSPECCION', 'El oficial en frontera rechazó el ingreso tras la fiscalización física.'),
('TODO_EN_ORDEN', 'Validaciones y fiscalizaciones completadas de manera exitosa.'),
('APROBADO', 'El trámite se encuentra visado y validado en su totalidad.'),
('FINALIZADO', 'El ciclo de vida del trámite concluyó (Cruce fronterizo completado).'),
('ACTUALIZADO', 'El trámite ha sufrido modificaciones en sus datos bases después del ingreso.');