package cl.aduana.ms_travel.dto.estado;

import org.springframework.stereotype.Component;
import cl.aduana.ms_travel.model.EstadoTramite;

@Component
public class EstadoTramiteMapper {
    public EstadoTramiteResponseDTO toResponse(EstadoTramite e) {
        return EstadoTramiteResponseDTO.builder()
            .tipoEstado(e.getEstado().getNombre())
            .fechaCambio(e.getFechaCambio())
            .comentario(e.getComentario())
            .build();
    }
}