package cl.aduana.ms_vehicle.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_vehicle.dto.VehiculoDTO;
import cl.aduana.ms_vehicle.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name= "Vehiculos", description="Gestión de vehículos en el sistema ADI")
public class VehiculoController {

    private final VehiculoService service;

    @Operation(summary="Listar todos los vehículos")
    @ApiResponse(responseCode= "200", description= "Lista obtenida correctamente")
    @GetMapping public List<VehiculoDTO> listar() { 
        return service.listar(); 
    }
    
    @Operation(summary= "Registrar un nuevo vehículo")
    @ApiResponses({
        @ApiResponse(responseCode= "201", description= "Vehículo creado correctamente"),
        @ApiResponse(responseCode= "400", description= "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<VehiculoDTO> guardar(@Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @Operation(summary= "Obtener vehículo por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Vehículo encontrado"),
        @ApiResponse(responseCode= "404", description= "Vehículo no encontrado")
    })
    @GetMapping("/{id}") 
    public VehiculoDTO obtener(
        @Parameter(description="ID del vehículo", required= true) @PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }

    @Operation(summary= "Obtener vehículo por patente")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Vehículo encontrado"),
        @ApiResponse(responseCode= "404", description= " Vehículo no encontrado")
    })
    @GetMapping("/patente/{patente}")
    public VehiculoDTO obtenerPorPatente(
        @Parameter(description="Patente del vehículo", required= true) @PathVariable String patente) {
        return service.obtenerPorPatente(patente);
    }

    @Operation(summary= "Actualizar vehículo")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Vehículo actualizado"),
        @ApiResponse(responseCode= "404", description= "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public VehiculoDTO actualizar(
        @Parameter(description="ID del vehículo", required=true) @PathVariable Long id, @Valid @RequestBody VehiculoDTO dto) {
        return service.actualizar(id, dto);
    }
    
    @Operation(summary= "Elimina vehículo por ID")
    @ApiResponses({
        @ApiResponse(responseCode= "200", description= "Vehículo eliminado correctamente"),
        @ApiResponse(responseCode= "404", description= "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @Parameter(description="ID del vehículo", required=true) @PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}