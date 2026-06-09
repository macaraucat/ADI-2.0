package cl.aduana.ms_vehicle.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_vehicle.dto.AdmisionTemporalDTO;
import cl.aduana.ms_vehicle.service.AdmisionTemporalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admisiones")
@RequiredArgsConstructor
@Tag(name= "AdmisionTemporal", description= "Gestiona las admisiones temporales")
public class AdmisionTemporalController {
    private final AdmisionTemporalService service;

    @Operation(summary= "Lista todas las admisiones temporales")
    @ApiResponse(responseCode= "200", description= "Lista obtenida correctamente")
    @GetMapping public List<AdmisionTemporalDTO> listar() { 
        return service.listar(); 
    }
    @Operation(summary= "Registrar una Admisión temporal")
    @ApiResponses({
        @ApiResponse(responseCode= "201", description= "Admisión temporal creada correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<AdmisionTemporalDTO> guardar(@Valid @RequestBody AdmisionTemporalDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary= "Obtener una Admisión temporal por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Admisión temporal encontrada"),
        @ApiResponse(responseCode= "404", description= "Admisión temporal no encontrada")
    })
    @GetMapping("/{id}") 
    public AdmisionTemporalDTO obtener(
        @Parameter(description= "ID de Admisión temporal", required= true) @PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }

    @Operation(summary= "Actualiza una admisión temporal por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Admisión temporal actualizada correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos invalidos")
    })
    @PutMapping("/{id}")
    public AdmisionTemporalDTO actualizar(
        @Parameter(description= "ID de Admisión temporal", required= true) @PathVariable Long id, @Valid @RequestBody AdmisionTemporalDTO dto) {
        return service.actualizar(id, dto);
    }

    @Operation(summary= "Eliminar Admisión temporal por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Admisión temporal eliminada correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos invalidos")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description= "ID de admisión temporal", required=true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}