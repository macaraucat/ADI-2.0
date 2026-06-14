package cl.aduana.ms_travel.dto.tramite;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import cl.aduana.ms_travel.dto.estado.EstadoTramiteResponseDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroResponseDTO;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.TipoTramite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
@JsonPropertyOrder({
    "idTramite", 
    "idUsuario", 
    "idVehiculo", 
    "tipoTramite", 
    "estadoGlobal", 
    "fechaDeIngreso", 
    "pasajeros", 
    "historialEstados"
})
public class TramiteResponseDTO {
    private Long idTramite;
    private Long idUsuario;
    private Long idVehiculo;
    private TipoTramite tipoTramite;
    private TipoEstado estadoGlobal;
    private LocalDateTime fechaDeIngreso;
    private List<PasajeroResponseDTO> pasajeros;
    private List<EstadoTramiteResponseDTO> historialEstados;
}