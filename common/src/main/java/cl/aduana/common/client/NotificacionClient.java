package cl.aduana.common.client;

import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;

@FeignClient(name = "ms-notificacion", url = "http://localhost:8086/api/v1/notificaciones")
public interface NotificacionClient {
    @PostMapping("/enviar")
    Optional<NotificacionResponseDTO> enviarMensaje(@RequestBody NotificacionRequestDTO request);
}