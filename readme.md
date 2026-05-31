**Principio clave:** Separación de responsabilidades. El common centraliza excepciones, DTOs y clients Feign. Ningún MS duplica código.
---

## 📦 Microservicios

### 1. common - Librería Compartida
**Responsabilidad:** Contrato único del sistema.
- **Excepciones:** BadRequestException (400), ResourceNotFoundException (404), InvalidStateException (409)
- **Clients Feign:** UsuarioClient, VehiculoClient, TravelClient, RiskClient, NotificacionClient
- **DTOs:** Request/Response para comunicación inter-servicios
- **Handler Global:** @RestControllerAdvice unificado

### 2. ms-login :8081 - Identidad y Accesos
**BD:** bd_login (usuario, rol, permiso, rol_permiso, usuario_rol)
- Autentica usuarios (POST /api/v1/login)
- Registra pasajeros y funcionarios (ADUANA, PDI, SAG)
- Gestiona roles: PASAJERO, AGENTE_ADUANA, AGENTE_PDI, AGENTE_SAG
- Valida permisos por documento
- **Endpoints clave:** /usuarios, /usuarios/{id}, /usuario/{doc}/permiso/{permiso}

### 3. ms-vehicle :8082 - Vehículos y Admisiones
**BD:** bd_vehiculos
- CRUD de vehículos (/api/v1/vehiculos)
- Admisiones temporales (/api/v1/admisiones)
- Conductores autorizados (/api/v1/conductores)
- Búsqueda por patente

### 4. ms-travel :8083 - Orquestador de Trámites
**BD:** bd_travel (tramite, pasajero, estado, estado_tramite)
- **Flujo principal:**
    1. Crea trámite → valida usuario (ms-login) y vehículo (ms-vehicle)
    2. Guarda pasajeros
    3. Notifica vía ms-notification
    4. Evalúa riesgo vía ms-risk → cambia estado automáticamente
- Gestiona estados: INGRESADO → APROBADO_AUTOMATICO/REQUIERE_INSPECCION/RECHAZADO_AUTOMATICO → APROBADO
- **Endpoints:** /api/v1/tramites, /tramites/{id}/pasajeros, /tramites/{id}/gestion/aprobar

### 5. ms-risk :8084 - Evaluación de Riesgo
**BD:** bd_alertas (inspeccion, alerta, criterio_riesgo, patente_infraccion)
- **Evaluación automática:** POST /api/v1/riesgo/evaluar
    - Regla: RUT inicia 8-9 = ALTO, 5-7 = MEDIO, 1-4 = BAJO
    - Verifica patente en lista negra
    - Valida existencia de trámite en ms-travel
- Gestiona inspecciones (/api/v1/inspeccion) - INICIADA → FINALIZADA
- Gestiona alertas (/api/v1/alerta)
- Precarga criterios de riesgo al iniciar

### 6. ms-notification :8086 - Notificaciones
**BD:** bd_notificaciones
- Envía notificaciones (POST /api/v1/notificaciones/enviar)
- Tipos: REGISTRO_EXITOSO, TRAMITE_CREADO
- Obtiene datos de usuario vía ms-login
- Simula envío de email (log)

### 7. ms-report :8085 - Reportes
**BD:** bd_reportes
- Genera reportes contando registros de otros MS
- **Tipos:** USUARIOS, VEHICULOS, TRAMITES, ALERTAS
- **Endpoint:** POST /api/v1/reportes/generar?tipo=USUARIOS&generadoPor=admin
- Usa clients del common (sin lógica propia)

---

## Flujo de Negocio Principal

1. **Usuario** se registra en ms-login → rol PASAJERO
2. **Crea trámite** en ms-travel con vehículo y pasajeros
3. ms-travel → valida en ms-login y ms-vehicle
4. ms-travel → notifica vía ms-notification
5. ms-travel → evalúa en ms-risk
6. ms-risk → devuelve BAJO/MEDIO/ALTO
7. Si ALTO → ms-travel marca RECHAZADO_AUTOMATICO
8. Si MEDIO → REQUIERE_INSPECCION → funcionario crea inspección en ms-risk
9. Funcionario aprueba → ms-travel cambia a APROBADO

---

## Stack Tecnológico

- **Java 17** + Spring Boot 3.2
- Spring Cloud OpenFeign, Spring Data JPA
- Lombok, Validation
- PostgreSQL
- SLF4J + logs estructurados
- Maven multi-módulo

