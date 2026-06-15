package cl.aduana.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class NotificacionRequestDTO {
    private Long idUsuario;
    private String tipoNotificacion;
    private String detalle;
}