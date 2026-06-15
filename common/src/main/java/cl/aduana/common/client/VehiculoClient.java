package cl.aduana.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.aduana.common.dto.response.VehiculoClientResponseDTO;

@FeignClient(name = "ms-vehicle", path = "/api/v1/vehiculos")
public interface VehiculoClient {
    @GetMapping("/{id}")
    VehiculoClientResponseDTO obtenerVehiculoPorId(@PathVariable("id") Long id);

    @GetMapping("")
    List<VehiculoClientResponseDTO> listarTodos();
}