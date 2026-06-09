package cl.aduana.ms_risk.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.ms_risk.service.RiesgoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/riesgo")
@RequiredArgsConstructor
@Tag(name = "Riesgo", description = "Evaluación de riesgo en el sistema ADI")
public class RiesgoController {

    private final RiesgoService service;

    @Operation(summary = "Evaluar riesgo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluación realizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/evaluar")
    public ResponseEntity<EvaluacionRiesgoResponseDTO> evaluar(@Valid @RequestBody EvaluacionRiesgoRequestDTO request) {
        return ResponseEntity.ok(service.evaluar(request));
    }
}