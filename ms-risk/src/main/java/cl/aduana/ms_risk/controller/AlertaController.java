package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_risk.dto.AlertaDTO;
import cl.aduana.ms_risk.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alerta")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Gestión de alertas en el sistema ADI")
public class AlertaController {

    private final AlertaService service;

    @Operation(summary = "Listar todas las alertas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<AlertaDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Registrar una nueva alerta")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alerta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<AlertaDTO> guardar(@Valid @RequestBody AlertaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary = "Obtener alerta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerta encontrada"),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @GetMapping("/{id}")
    public AlertaDTO obtener(
        @Parameter(description = "ID de la alerta", required = true) @PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Actualizar alerta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerta actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @PutMapping("/{id}")
    public AlertaDTO actualizar(
        @Parameter(description = "ID de la alerta", required = true) @PathVariable Long id,
        @Valid @RequestBody AlertaDTO dto) {
        return service.actualizar(id, dto);
    }

    @Operation(summary = "Eliminar alerta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Alerta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la alerta", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}