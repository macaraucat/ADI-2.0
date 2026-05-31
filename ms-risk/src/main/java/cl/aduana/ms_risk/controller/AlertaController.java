package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_risk.dto.AlertaDTO;
import cl.aduana.ms_risk.service.AlertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/alerta")
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService service;

    @GetMapping
    public List<AlertaDTO> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<AlertaDTO> guardar(@Valid @RequestBody AlertaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @GetMapping("/{id}")
    public AlertaDTO obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public AlertaDTO actualizar(@PathVariable Long id, @Valid @RequestBody AlertaDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}