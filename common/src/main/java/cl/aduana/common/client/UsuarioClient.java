package cl.aduana.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.aduana.common.dto.response.UsuarioClientResponseDTO;

@FeignClient(name = "ms-usuario", path = "/api/v1/login")
public interface UsuarioClient {
    @GetMapping("/usuarios/{id}")
    UsuarioClientResponseDTO obtenerUsuarioPorId(@PathVariable("id") Long id);

    @GetMapping("/usuarios")
    List<UsuarioClientResponseDTO> listarTodos();    
}