package cl.aduana.ms_report.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_report.model.Reporte;
import cl.aduana.ms_report.model.TipoReporte;
import cl.aduana.ms_report.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Reportes", description = "API para generación, consulta y gestión de reportes del sistema de aduana.")
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private final ReporteService service;

    // 1. LISTAR REPORTES
    @Operation(summary = "Listar todos los reportes", description = "Obtiene el listado completo de reportes generados en el sistema. "
            +
            "Incluye todos los reportes sin filtros, independientemente de su antigüedad.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reporte.class), 
            examples = @ExampleObject(name = "Lista de reportes", value = "["
                    +
                    "{\"id\": 1, \"tipo\": \"USUARIOS\", \"fechaGeneracion\": \"2024-01-15T14:30:00\", " +
                    "\"totalRegistros\": 150, \"generadoPor\": \"juan.garcia@aduana.cl\", \"formato\": \"JSON\"}," +
                    "{\"id\": 2, \"tipo\": \"VEHICULOS\", \"fechaGeneracion\": \"2024-01-15T15:45:00\", " +
                    "\"totalRegistros\": 320, \"generadoPor\": \"maria.lopez@aduana.cl\", \"formato\": \"JSON\"}" +
                    "]")))
    })
    @GetMapping("")
    public ResponseEntity<List<Reporte>> listar() {
        log.info("GET /api/v1/reportes - Solicitud de listado de reportes");
        List<Reporte> reportes = service.listar();
        log.info("Listado completado: {} reportes encontrados", reportes.size());
        return ResponseEntity.ok(reportes);
    }

    // 2. OBTENER REPORTE POR ID
    @Operation(summary = "Obtener reporte por ID", description = "Busca un reporte específico utilizando su ID interno.")
    @Parameter(name = "id", description = "ID único del reporte a consultar", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reporte encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reporte.class), 
            examples = @ExampleObject(name = "Reporte encontrado", value = "{\"id\": 1, \"tipo\": \"USUARIOS\", " +
                    "\"fechaGeneracion\": \"2024-01-15T14:30:00\", " +
                    "\"totalRegistros\": 150, \"generadoPor\": \"juan.garcia@aduana.cl\", " +
                    "\"formato\": \"JSON\"}"))),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(mediaType = "application/json", 
            examples = @ExampleObject(name = "Error 404", value = "{\"error\": \"Recurso no encontrado\", " +
                    "\"message\": \"Reporte id 999 no encontrado\", " +
                    "\"timestamp\": \"2024-01-15T14:30:00\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Reporte> obtener(@PathVariable Long id) {
        log.info("🔍 GET /api/v1/reportes/{} - Buscando reporte por ID", id);
        try {
            Reporte reporte = service.obtener(id);
            log.info("Reporte encontrado: tipo={}, generadoPor={}, registros={}",
                    reporte.getTipo(), reporte.getGeneradoPor(), reporte.getTotalRegistros());
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            log.warn("Error al obtener reporte {}: {}", id, e.getMessage());
            throw e;
        }
    }

    // 3. GENERAR NUEVO REPORTE
    @Operation(summary = "Generar nuevo reporte", description = "Crea y genera un nuevo reporte según el tipo especificado.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reporte generado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reporte.class), 
                examples = @ExampleObject(name = "Reporte generado", value = "{\"id\": 3, \"tipo\": \"USUARIOS\", " +
                    "\"fechaGeneracion\": \"2024-01-15T16:00:00\", " +
                    "\"totalRegistros\": 150, \"generadoPor\": \"carlos.mendez@aduana.cl\", " +
                    "\"formato\": \"JSON\"}"))),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos o faltantes", content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(name = "Error 400", value = "{\"error\": \"Verifique los datos enviados\", " +
                    "\"message\": \"Tipo obligatorio\", " +
                    "\"timestamp\": \"2024-01-15T16:00:00\"}"))),
            @ApiResponse(responseCode = "500", description = "Error al consultar los datos de los microservicios")
    })
    @PostMapping("/generar")
    public ResponseEntity<Reporte> generar(
            @Parameter(name = "tipo", description = "Tipo de reporte a generar.", example = "USUARIOS", required = true) 
            @RequestParam TipoReporte tipo,
            @Parameter(name = "generadoPor", description = "Identificación del funcionario/usuario que genera el reporte. (E-mail, Nro. Doc. o ID.", example = "juan.garcia@aduana.cl", required = true) 
            @RequestParam @NotBlank(message = "El parámetro 'generadoPor' es obligatorio") String generadoPor) {

        log.info("POST /api/v1/reportes/generar - Iniciando generación");
        log.info("Tipo: {}", tipo);
        log.info("Generado por: {}", generadoPor);

        try {
            long tiempoInicio = System.currentTimeMillis();
            Reporte reporte = service.generar(tipo, generadoPor);
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;

            log.info("Reporte generado exitosamente:");
            log.info("ID: {}", reporte.getId());
            log.info("Tipo: {}", reporte.getTipo());
            log.info("Total de registros: {}", reporte.getTotalRegistros());
            log.info("Formato: {}", reporte.getFormato());
            log.info("Fecha: {}", reporte.getFechaGeneracion());
            log.info("Tiempo de generación: {}ms", tiempoTranscurrido);

            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            log.error("Error al generar reporte de tipo {}: {}", tipo, e.getMessage());
            throw e;
        }
    }

    // 4. ELIMINAR REPORTE
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte específico del sistema de forma permanente.")
    @Parameter(name = "id", description = "ID único del reporte a eliminar", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reporte eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado", content = @Content(mediaType = "application/json", 
                examples = @ExampleObject(name = "Error 404", value = "{\"error\": \"Recurso no encontrado\", \"message\": \"Reporte id 999 no encontrado\", \"timestamp\": \"2024-01-15T16:00:00\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /api/v1/reportes/{} - Solicitud de eliminación", id);
        try {
            service.eliminar(id);
            log.info("Reporte eliminado correctamente: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar reporte {}: {}", id, e.getMessage());
            throw e;
        }
    }
}