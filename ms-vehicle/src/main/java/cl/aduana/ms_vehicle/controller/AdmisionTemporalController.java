package cl.aduana.ms_vehicle.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cl.aduana.ms_vehicle.dto.AdmisionTemporalDTO;
import cl.aduana.ms_vehicle.service.AdmisionTemporalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admisiones")
@RequiredArgsConstructor
public class AdmisionTemporalController {
    private final AdmisionTemporalService service;

    @GetMapping public List<AdmisionTemporalDTO> listar() { 
        return service.listar(); 
    }
    
    @PostMapping
    public ResponseEntity<AdmisionTemporalDTO> guardar(@Valid @RequestBody AdmisionTemporalDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{id}") public AdmisionTemporalDTO obtener(@PathVariable Long id) { 
        return service.obtenerPorId(id); 
    }
    
    @PutMapping("/{id}")
    public AdmisionTemporalDTO actualizar(@PathVariable Long id, @Valid @RequestBody AdmisionTemporalDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}