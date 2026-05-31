package cl.aduana.ms_travel.dto.pasajero;

import org.springframework.stereotype.Component;
import cl.aduana.ms_travel.model.Pasajero;

@Component
public class PasajeroMapper {
    public Pasajero toEntity(PasajeroRequestDTO r) {
        return Pasajero.builder()
            .tipoDocumento(r.getTipoDocumento())
            .nroDocumento(r.getNroDocumento())
            .nacionalidad(r.getNacionalidad())
            .fechaNacimiento(r.getFechaNacimiento())
            .build();
    }

    public PasajeroResponseDTO toResponse(Pasajero p) {
        return PasajeroResponseDTO.builder()
            .idPasajero(p.getIdPasajero())
            .tipoDocumento(p.getTipoDocumento())
            .nroDocumento(p.getNroDocumento())
            .nacionalidad(p.getNacionalidad())
            .fechaNacimiento(p.getFechaNacimiento())
            .build();
    }

    public Pasajero updateEntityFromRequest(Pasajero pasajeroExistente, PasajeroRequestDTO request) {
        if (request == null) {
            return pasajeroExistente;
        }
        if (request.getTipoDocumento() != null) {
            pasajeroExistente.setTipoDocumento(request.getTipoDocumento());
        }
        if (request.getNroDocumento() != null) {
            pasajeroExistente.setNroDocumento(request.getNroDocumento());
        }
        if (request.getNacionalidad() != null) {
            pasajeroExistente.setNacionalidad(request.getNacionalidad());
        }
        if (request.getFechaNacimiento() != null) {
            pasajeroExistente.setFechaNacimiento(request.getFechaNacimiento());
        }
        return pasajeroExistente;
    }
}