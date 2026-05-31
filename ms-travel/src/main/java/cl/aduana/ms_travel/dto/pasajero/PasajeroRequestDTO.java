package cl.aduana.ms_travel.dto.pasajero;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PasajeroRequestDTO {
    @NotBlank private String tipoDocumento;
    @NotBlank private String nroDocumento;
    @NotBlank private String nacionalidad;
    @NotNull private LocalDate fechaNacimiento;
}