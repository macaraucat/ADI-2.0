package cl.aduana.ms_vehicle.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_vehicle.dto.ConductorAutorizadoDTO;
import cl.aduana.ms_vehicle.service.ConductorAutorizadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/conductores")
@RequiredArgsConstructor
@Tag(name= "Conductor", description= "Gestiona los conductores")
public class ConductorAutorizadoController {
    private final ConductorAutorizadoService service;

    @Operation(summary= "Lista todos los conductores")
    @ApiResponse(responseCode= "200", description= "Lista obtenida correctamente")
    @GetMapping public List<ConductorAutorizadoDTO> listar() { 
        return service.listar(); 
    }
    
    @Operation(summary= "Registra a un conductor")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Conductor registrado correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ConductorAutorizadoDTO> guardar(@Valid @RequestBody ConductorAutorizadoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary= "Obtiene un conductor por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Conductor encontrado"),
        @ApiResponse(responseCode= "404", description= "Datos inválidos")
    })
    @GetMapping("/{id}") public ConductorAutorizadoDTO obtener(
        @Parameter(description= "ID del conductor", required= true) @PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }

    @Operation(summary= "Modifica al conductor por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Conductor modificado correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ConductorAutorizadoDTO actualizar(
        @Parameter(description= "ID del conductor", required=true) @PathVariable Long id, @Valid @RequestBody ConductorAutorizadoDTO dto) {
        return service.actualizar(id, dto);
    }

    @Operation(summary= "Elimina conductor por ID")
    @ApiResponses ({
        @ApiResponse(responseCode= "200", description= "Conductor eliminado correctamente"),
        @ApiResponse(responseCode= "404", description= "Conductor no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description= "ID del conductor", required=true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}