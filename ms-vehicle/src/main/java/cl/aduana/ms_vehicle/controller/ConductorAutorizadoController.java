package cl.aduana.ms_vehicle.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.aduana.ms_vehicle.dto.ConductorAutorizadoDTO;
import cl.aduana.ms_vehicle.service.ConductorAutorizadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/conductores")
@RequiredArgsConstructor
public class ConductorAutorizadoController {
    private final ConductorAutorizadoService service;

    @GetMapping public List<ConductorAutorizadoDTO> listar() { 
        return service.listar(); 
    }
    
    @PostMapping
    public ResponseEntity<ConductorAutorizadoDTO> guardar(@Valid @RequestBody ConductorAutorizadoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{id}") public ConductorAutorizadoDTO obtener(@PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }
    
    @PutMapping("/{id}")
    public ConductorAutorizadoDTO actualizar(@PathVariable Long id, @Valid @RequestBody ConductorAutorizadoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}