package cl.aduana.ms_travel.dto.tramite;

import org.springframework.stereotype.Component;

import cl.aduana.ms_travel.dto.estado.EstadoTramiteMapper;
import cl.aduana.ms_travel.dto.pasajero.PasajeroMapper;
import cl.aduana.ms_travel.model.Tramite;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TramiteMapper {

    private final PasajeroMapper pasajeroMapper;
    private final EstadoTramiteMapper estadoTramiteMapper;

    public Tramite toEntity(TramiteRequestDTO tramiteRequest) {
        return Tramite.builder()
                .idUsuario(tramiteRequest.getIdUsuario())
                .idVehiculo(tramiteRequest.getIdVehiculo())
                .tipoTramite(tramiteRequest.getTipoTramite())
                .build();
    }

    public TramiteResponseDTO toResponse(Tramite tramite) {
        return TramiteResponseDTO.builder()
        .idTramite(tramite.getIdTramite())
        .idUsuario(tramite.getIdUsuario())
        .idVehiculo(tramite.getIdVehiculo())
        .tipoTramite(tramite.getTipoTramite())
        .estadoGlobal(tramite.getEstadoActual())
        .fechaDeIngreso(tramite.getFechaDeIngreso())
        .pasajeros(tramite.getPasajeros().stream().map(pasajeroMapper::toResponse).toList())
        .historialEstados(tramite.getHistorialEstados().stream().map(estadoTramiteMapper::toResponse).toList())
        .build();
    }

    public TramiteResumenResponseDTO toResumenResponse (Tramite tramite) {
        return TramiteResumenResponseDTO.builder()
        .idTramite(tramite.getIdTramite())
        .idUsuario(tramite.getIdUsuario())
        .idVehiculo(tramite.getIdVehiculo())
        .tipoTramite(tramite.getTipoTramite())
        .estadoGlobal(tramite.getEstadoActual())
        .fechaDeIngreso(tramite.getFechaDeIngreso())
        .build();
    }

    public Tramite updateEntityFromRequest(Tramite tramiteExistente, TramiteRequestUpdateDTO tramiteRequestUpdate) {
    if (tramiteRequestUpdate.getIdUsuario() != null) {
        tramiteExistente.setIdUsuario(tramiteRequestUpdate.getIdUsuario());
    }
    if (tramiteRequestUpdate.getIdVehiculo() != null) {
        tramiteExistente.setIdVehiculo(tramiteRequestUpdate.getIdVehiculo());
    }
    if (tramiteRequestUpdate.getTipoTramite() != null) {
        tramiteExistente.setTipoTramite(tramiteRequestUpdate.getTipoTramite());
    }
    return tramiteExistente;
    }
}