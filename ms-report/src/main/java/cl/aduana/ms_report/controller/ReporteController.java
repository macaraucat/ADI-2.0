package cl.aduana.ms_report.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_report.model.Reporte;
import cl.aduana.ms_report.model.TipoReporte;
import cl.aduana.ms_report.service.ReporteService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private final ReporteService service;

    @GetMapping("")
    public ResponseEntity<List<Reporte>> listar() {
        log.info("GET /api/v1/reportes - Solicitud de listado de reportes");
        List<Reporte> reportes = service.listar();
        log.info("Listado completado: {} reportes encontrados", reportes.size());
        return ResponseEntity.ok(reportes);
    }

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

    @PostMapping("/generar")
    public ResponseEntity<Reporte> generar(
            @RequestParam TipoReporte tipo,
            @RequestParam @NotBlank String generadoPor) {
        
        log.info("POST /api/v1/reportes/generar - Iniciando generación");
        log.info("Tipo: {}", tipo);
        log.info("Generado por: {}", generadoPor);
        
        try {
            Reporte reporte = service.generar(tipo, generadoPor);
            log.info("Reporte generado exitosamente:");
            log.info("ID: {}", reporte.getId());
            log.info("Tipo: {}", reporte.getTipo());
            log.info("Total de registros: {}", reporte.getTotalRegistros());
            log.info("Formato: {}", reporte.getFormato());
            log.info("Fecha: {}", reporte.getFechaGeneracion());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
        } catch (Exception e) {
            log.error("Error al generar reporte de tipo {}: {}", tipo, e.getMessage());
            throw e;
        }
    }

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