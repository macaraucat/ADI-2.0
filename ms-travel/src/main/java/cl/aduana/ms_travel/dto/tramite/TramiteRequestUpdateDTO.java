package cl.aduana.ms_travel.dto.tramite;

import cl.aduana.ms_travel.model.TipoTramite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TramiteRequestUpdateDTO {
    private Long idUsuario;
    private Long idVehiculo;
    private TipoTramite tipoTramite;
}