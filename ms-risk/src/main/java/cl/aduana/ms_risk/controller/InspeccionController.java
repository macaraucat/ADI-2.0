package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_risk.dto.InspeccionRequestDTO;
import cl.aduana.ms_risk.dto.InspeccionResponseDTO;
import cl.aduana.ms_risk.dto.PatchInspeccionDTO;
import cl.aduana.ms_risk.service.InspeccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inspeccion")
@RequiredArgsConstructor
public class InspeccionController {

    private final InspeccionService service;

    @GetMapping
    public List<InspeccionResponseDTO> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<InspeccionResponseDTO> guardar(@Valid @RequestBody InspeccionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{id}")
    public InspeccionResponseDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PatchMapping("/{id}")
    public InspeccionResponseDTO finalizar(@PathVariable Long id, @Valid @RequestBody PatchInspeccionDTO patch) {
        return service.finalizar(id, patch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}