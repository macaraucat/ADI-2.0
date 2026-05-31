package cl.aduana.common.dto.request;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class NotificacionRequestDTO {
    private Long idUsuario;
    private String tipoNotificacion;
    private String detalle;
}