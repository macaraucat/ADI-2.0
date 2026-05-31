package cl.aduana.ms_travel.dto.pasajero;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PasajeroResponseDTO {
    private Long idPasajero;
    private String tipoDocumento;
    private String nroDocumento;
    private String nacionalidad;
    private LocalDate fechaNacimiento;
}