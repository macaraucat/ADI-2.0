package cl.aduana.common.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.aduana.common.dto.response.UsuarioClientResponseDTO;

@FeignClient(name = "ms-usuario", url = "http://localhost:8081/api/v1/login/usuarios")
public interface UsuarioClient {
    @GetMapping("/{id}")
    UsuarioClientResponseDTO obtenerUsuarioPorId(@PathVariable("id") Long id);

    @GetMapping("")
    List<UsuarioClientResponseDTO> listarTodos();    
}