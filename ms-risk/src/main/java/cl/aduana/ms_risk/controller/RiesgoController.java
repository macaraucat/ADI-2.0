package cl.aduana.ms_risk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.ms_risk.service.RiesgoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/riesgo")
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService service;

    @PostMapping("/evaluar")
    public ResponseEntity<EvaluacionRiesgoResponseDTO> evaluar(@Valid @RequestBody EvaluacionRiesgoRequestDTO request) {
        return ResponseEntity.ok(service.evaluar(request));
    }
}