package cl.aduana.common.client;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.AlertaClientResponseDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;

@FeignClient(name = "ms-risk", path = "/api/v1/riesgo")
public interface RiskClient {
    @PostMapping("/evaluar")
    Optional<EvaluacionRiesgoResponseDTO> evaluarRiesgo(@RequestBody EvaluacionRiesgoRequestDTO request);

    @GetMapping("/listar")
    List<AlertaClientResponseDTO> listarAlertas();
}