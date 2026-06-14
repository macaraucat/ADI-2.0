package cl.aduana.ms_travel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.aduana.ms_travel.dto.estado.EstadoTramiteResponseDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroRequestDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestUpdateDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResumenResponseDTO;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.service.TramiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/tramites")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Trámites", description = "Endpoints para gestión de trámites de viaje")
public class TramiteController {

    private final TramiteService service;

    // LISTAR
    @Operation( //documentación OpenAPI para listar trámites con ejemplos y descripciones detalladas
        summary = "Listar trámites",
        description = "Obtiene una lista de trámites, opcionalmente filtrados por estado",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de trámites",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = TramiteResumenResponseDTO.class)),
                    examples = @ExampleObject(
                        name = "TramiteResumenLista",
                        externalValue = "classpath:/openapi/examples/tramite-resumen-response.json"
                    )
                )
            )
        }
    )
    @GetMapping
    public ResponseEntity<List<TramiteResumenResponseDTO>> listar(
            @Parameter(description = "Estados para filtrar el listado", example = "INGRESADO,APROBADO")
            @RequestParam(required = false) List<TipoEstado> estados) {
        log.info("GET /tramites - estados={}", estados);
        return ResponseEntity.ok(service.listarTodos(estados));
    }

    @Operation(
        summary = "Obtener trámite por ID",
        description = "Obtiene los detalles de un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle del trámite",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteDetalle",
                        externalValue = "classpath:/openapi/examples/tramite-crear-response.json"
                    )
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TramiteResponseDTO> obtener(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id) {
        log.info("GET /tramites/{} - buscando", id);
        return ResponseEntity.ok(service.findById(id));
    }

    // CREAR
    @Operation(
        summary = "Crear trámite",
        description = "Crea un nuevo trámite de viaje",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Trámite creado con éxito",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteCrearResponse",
                        externalValue = "classpath:/openapi/examples/tramite-crear-response.json"
                    )
                )
            )
        }
    )
    @PostMapping
    public ResponseEntity<TramiteResponseDTO> crear(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Payload para crear un trámite",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteRequestDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteCrearRequest",
                        externalValue = "classpath:/openapi/examples/tramite-crear-request.json"
                    )
                )
            )
            @RequestBody TramiteRequestDTO request) {
        log.info("POST /tramites - nuevo tramite usuario={} vehiculo={}", 
                 request.getIdUsuario(), request.getIdVehiculo());
        TramiteResponseDTO creado = service.crearNuevoTramite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ACTUALIZAR
    @Operation(
        summary = "Actualizar trámite",
        description = "Actualiza los datos de un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Resumen del trámite actualizado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteResumenResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteActualizarResponse",
                        externalValue = "classpath:/openapi/examples/tramite-resumen-response.json"
                    )
                )
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<TramiteResumenResponseDTO> actualizar(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Payload de actualización del trámite",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteRequestUpdateDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteActualizarRequest",
                        externalValue = "classpath:/openapi/examples/tramite-actualizar-request.json"
                    )
                )
            )
            @RequestBody TramiteRequestUpdateDTO request) {
        log.info("PUT /tramites/{} - actualizando", id);
        return ResponseEntity.ok(service.actualizarTramite(id, request));
    }

    // CANCELAR
    @Operation(
        summary = "Cancelar trámite",
        description = "Cancela un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Estado del trámite tras cancelación",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EstadoTramiteResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteCancelarResponse",
                        externalValue = "classpath:/openapi/examples/tramite-estado-response.json"
                    )
                )
            )
        }
    )
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EstadoTramiteResponseDTO> cancelar(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id) {
        log.warn("PATCH /tramites/{}/cancelar", id);
        return ResponseEntity.ok(service.cancelarTramite(id));
    }

    // APROBAR
    @Operation(
        summary = "Aprobar trámite",
        description = "Aprueba un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Estado del trámite tras aprobación",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = EstadoTramiteResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteAprobarResponse",
                        externalValue = "classpath:/openapi/examples/tramite-estado-response.json"
                    )
                )
            )
        }
    )
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<EstadoTramiteResponseDTO> aprobar(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id) {
        log.info("PATCH /tramites/{}/aprobar", id);
        return ResponseEntity.ok(service.aprobarTramite(id));
    }

    // EVALUAR RIESGO MANUAL
    @Operation(
        summary = "Evaluar riesgo",
        description = "Evalúa el riesgo de un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle del trámite evaluado",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TramiteResponseDTO.class),
                    examples = @ExampleObject(
                        name = "TramiteEvaluarRiesgoResponse",
                        externalValue = "classpath:/openapi/examples/tramite-crear-response.json"
                    )
                )
            )
        }
    )
    @PostMapping("/{id}/evaluar-riesgo")
    public ResponseEntity<TramiteResponseDTO> evaluarRiesgo(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id) {
        log.info("POST /tramites/{}/evaluar-riesgo", id);
        return ResponseEntity.ok(service.evaluarRiesgoTramite(id));
    }

    // BUSCAR POR PARAMETROS
    @Operation(
        summary = "Buscar trámites",
        description = "Busca trámites por parámetros específicos",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de trámites que coinciden con los parámetros",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = TramiteResumenResponseDTO.class)),
                    examples = @ExampleObject(
                        name = "TramiteBuscarLista",
                        externalValue = "classpath:/openapi/examples/tramite-resumen-response.json"
                    )
                )
            )
        }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<TramiteResumenResponseDTO>> buscar(
            @Parameter(description = "ID de usuario para filtrar", example = "100")
            @RequestParam(required = false) Long idUsuario,
            @Parameter(description = "ID de vehículo para filtrar", example = "200")
            @RequestParam(required = false) Long idVehiculo) {
        log.info("GET /tramites/buscar - usuario={} vehiculo={}", idUsuario, idVehiculo);
        return ResponseEntity.ok(service.buscarPorParametros(idUsuario, idVehiculo));
    }

    // --- PASAJEROS ---

    @Operation(
        summary = "Listar pasajeros",
        description = "Obtiene la lista de pasajeros asociados a un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de pasajeros asociados al trámite",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = PasajeroResponseDTO.class)),
                    examples = @ExampleObject(
                        name = "PasajeroListaResponse",
                        externalValue = "classpath:/openapi/examples/pasajero-response.json"
                    )
                )
            )
        }
    )
    @GetMapping("/{id}/pasajeros")
    public ResponseEntity<List<PasajeroResponseDTO>> listarPasajeros(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id) {
        log.info("GET /tramites/{}/pasajeros", id);
        return ResponseEntity.ok(service.listarPasajeros(id));
    }

    @Operation(
        summary = "Agregar pasajero",
        description = "Agrega un pasajero a un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Pasajero agregado al trámite",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PasajeroResponseDTO.class),
                    examples = @ExampleObject(
                        name = "PasajeroAgregarResponse",
                        externalValue = "classpath:/openapi/examples/pasajero-response.json"
                    )
                )
            )
        }
    )
    @PostMapping("/{id}/pasajeros")
    public ResponseEntity<PasajeroResponseDTO> agregarPasajero(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Payload para agregar un pasajero",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PasajeroRequestDTO.class),
                    examples = @ExampleObject(
                        name = "PasajeroAgregarRequest",
                        externalValue = "classpath:/openapi/examples/pasajero-request.json"
                    )
                )
            )
            @RequestBody PasajeroRequestDTO request) {
        log.info("POST /tramites/{}/pasajeros - doc={}", id, request.getNroDocumento());
        PasajeroResponseDTO resp = service.agregarPasajero(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(
        summary = "Modificar pasajero",
        description = "Modifica los datos de un pasajero específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Pasajero modificado exitosamente",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PasajeroResponseDTO.class),
                    examples = @ExampleObject(
                        name = "PasajeroModificarResponse",
                        externalValue = "classpath:/openapi/examples/pasajero-response.json"
                    )
                )
            )
        }
    )
    @PutMapping("/{id}/pasajeros/{idPasajero}")
    public ResponseEntity<PasajeroResponseDTO> modificarPasajero(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del pasajero", required = true, example = "1")
            @PathVariable Long idPasajero,
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Payload para modificar un pasajero",
                required = true,
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PasajeroRequestDTO.class),
                    examples = @ExampleObject(
                        name = "PasajeroModificarRequest",
                        externalValue = "classpath:/openapi/examples/pasajero-request.json"
                    )
                )
            )
            @RequestBody PasajeroRequestDTO request) {
        log.info("PUT /tramites/{}/pasajeros/{}", id, idPasajero);
        return ResponseEntity.ok(service.modificarPasajero(id, idPasajero, request));
    }

    @Operation(
        summary = "Quitar pasajero",
        description = "Quita un pasajero de un trámite específico por su ID",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Mensaje de confirmación de eliminación",
                content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)
                )
            )
        }
    )
    @DeleteMapping("/{id}/pasajeros/{idPasajero}")
    public ResponseEntity<String> quitarPasajero(
            @Parameter(description = "ID del trámite", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "ID del pasajero", required = true, example = "1")
            @PathVariable Long idPasajero) {
        log.warn("DELETE /tramites/{}/pasajeros/{}", id, idPasajero);
        return ResponseEntity.ok(service.quitarPasajero(id, idPasajero));
    }
}
