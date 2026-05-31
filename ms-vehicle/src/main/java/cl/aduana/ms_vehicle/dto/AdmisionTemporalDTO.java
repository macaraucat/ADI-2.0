package cl.aduana.ms_vehicle.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdmisionTemporalDTO {
    private Long idAdmisionTemporal;
    private LocalDate fechaSalida;
    private LocalDate fechaVencimiento;
    @NotBlank private String pasoSalida;
    @NotBlank private String estado;
    @NotNull private Long idVehiculo;
}