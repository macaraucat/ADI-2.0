package cl.aduana.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.aduana.common.dto.response.TramiteClientResponseDTO;

@FeignClient(name = "ms-travel", url = "http://localhost:8083/api/v1/tramites")
public interface TravelClient {
    @GetMapping("")
    List<TramiteClientResponseDTO> listarTodos();

    @GetMapping("/{id}")
    TramiteClientResponseDTO obtenerTramitePorId(@PathVariable("id") Long id);    
}