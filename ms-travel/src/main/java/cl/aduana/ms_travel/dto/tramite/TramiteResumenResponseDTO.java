package cl.aduana.ms_travel.dto.tramite;

import java.time.LocalDateTime;

import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.TipoTramite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TramiteResumenResponseDTO {
    private Long idTramite;
    private Long idUsuario;
    private Long idVehiculo;
    private TipoTramite tipoTramite;
    private TipoEstado estadoGlobal;
    private LocalDateTime fechaDeIngreso;
}