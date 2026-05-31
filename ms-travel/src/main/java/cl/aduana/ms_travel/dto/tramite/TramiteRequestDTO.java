package cl.aduana.ms_travel.dto.tramite;

import java.util.List;
import cl.aduana.ms_travel.dto.pasajero.PasajeroRequestDTO;
import cl.aduana.ms_travel.model.TipoTramite;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TramiteRequestDTO {
    @NotNull private Long idUsuario;
    @NotNull private Long idVehiculo;
    @NotNull private TipoTramite tipoTramite;
    @Valid @NotNull private List<PasajeroRequestDTO> pasajeros;
}