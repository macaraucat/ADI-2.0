package cl.aduana.ms_vehicle.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.aduana.ms_vehicle.dto.VehiculoDTO;
import cl.aduana.ms_vehicle.service.VehiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    private final VehiculoService service;

    @GetMapping public List<VehiculoDTO> listar() { 
        return service.listar(); 
    }
    
    @PostMapping
    public ResponseEntity<VehiculoDTO> guardar(@Valid @RequestBody VehiculoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{id}") public VehiculoDTO obtener(@PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }
    
    @GetMapping("/patente/{patente}")
    public VehiculoDTO obtenerPorPatente(@PathVariable String patente) {
        return service.obtenerPorPatente(patente);
    }

    @PutMapping("/{id}")
    public VehiculoDTO actualizar(@PathVariable Long id, @Valid @RequestBody VehiculoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}