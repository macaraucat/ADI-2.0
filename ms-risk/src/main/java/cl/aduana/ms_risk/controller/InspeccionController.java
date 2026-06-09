package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_risk.dto.InspeccionRequestDTO;
import cl.aduana.ms_risk.dto.InspeccionResponseDTO;
import cl.aduana.ms_risk.dto.PatchInspeccionDTO;
import cl.aduana.ms_risk.service.InspeccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspeccion")
@RequiredArgsConstructor
@Tag(name = "Inspecciones", description = "Gestión de inspecciones en el sistema ADI")
public class InspeccionController {

    private final InspeccionService service;

    @Operation(summary = "Listar todas las inspecciones")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public List<InspeccionResponseDTO> listar() {
        return service.listar();
    }

    @Operation(summary = "Registrar una nueva inspección")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inspección creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<InspeccionResponseDTO> guardar(@Valid @RequestBody InspeccionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary = "Obtener inspección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inspección encontrada"),
        @ApiResponse(responseCode = "404", description = "Inspección no encontrada")
    })
    @GetMapping("/{id}")
    public InspeccionResponseDTO obtener(
        @Parameter(description = "ID de la inspección", required = true) @PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @Operation(summary = "Finalizar inspección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inspección finalizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Inspección no encontrada")
    })
    @PatchMapping("/{id}")
    public InspeccionResponseDTO finalizar(
        @Parameter(description = "ID de la inspección", required = true) @PathVariable Long id,
        @Valid @RequestBody PatchInspeccionDTO patch) {
        return service.finalizar(id, patch);
    }

    @Operation(summary = "Eliminar inspección por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Inspección eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Inspección no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description = "ID de la inspección", required = true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}