package cl.aduana.ms_travel.dto.estado;

import java.time.LocalDateTime;
import cl.aduana.ms_travel.model.TipoEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EstadoTramiteResponseDTO {
    private TipoEstado tipoEstado;
    private LocalDateTime fechaCambio;
    private String comentario;
}